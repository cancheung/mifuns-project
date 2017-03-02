package com.mifuns.db.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Constants;
import org.springframework.transaction.*;
import org.springframework.transaction.support.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

/**
 * <p>Created with IntelliJ IDEA. </p>
 * <p>User: Stony </p>
 * <p>Date: 2016/8/5 </p>
 * <p>Time: 15:47 </p>
 * <p>Version: 1.0.1 </p>
 */
public abstract class AbstractRoutingTransactionManager implements PlatformTransactionManager, Serializable {
    /**
     * Always activate transaction synchronization, even for "empty" transactions
     * that result from PROPAGATION_SUPPORTS with no existing backend transaction.
     * @see TransactionDefinition#PROPAGATION_SUPPORTS
     * @see TransactionDefinition#PROPAGATION_NOT_SUPPORTED
     * @see TransactionDefinition#PROPAGATION_NEVER
     */
    public static final int SYNCHRONIZATION_ALWAYS = 0;

    /**
     * Activate transaction synchronization only for actual transactions,
     * that is, not for empty ones that result from PROPAGATION_SUPPORTS with
     * no existing backend transaction.
     * @see TransactionDefinition#PROPAGATION_REQUIRED
     * @see TransactionDefinition#PROPAGATION_MANDATORY
     * @see TransactionDefinition#PROPAGATION_REQUIRES_NEW
     */
    public static final int SYNCHRONIZATION_ON_ACTUAL_TRANSACTION = 1;

    /**
     * Never active transaction synchronization, not even for actual transactions.
     */
    public static final int SYNCHRONIZATION_NEVER = 2;


    /** Constants instance for AbstractPlatformTransactionManager */
    private static final Constants constants = new Constants(AbstractPlatformTransactionManager.class);


    protected transient Log logger = LogFactory.getLog(getClass());

    private int transactionSynchronization = SYNCHRONIZATION_ALWAYS;

    private int defaultTimeout = TransactionDefinition.TIMEOUT_DEFAULT;

    private boolean nestedTransactionAllowed = false;

    private boolean validateExistingTransaction = false;

    private boolean globalRollbackOnParticipationFailure = true;

    private boolean failEarlyOnGlobalRollbackOnly = false;

    private boolean rollbackOnCommitFailure = false;


    /**
     * Set the transaction synchronization by the name of the corresponding constant
     * in this class, e.g. "SYNCHRONIZATION_ALWAYS".
     * @param constantName name of the constant
     * @see #SYNCHRONIZATION_ALWAYS
     */
    public final void setTransactionSynchronizationName(String constantName) {
        setTransactionSynchronization(constants.asNumber(constantName).intValue());
    }

    /**
     * Set when this transaction manager should activate the thread-bound
     * transaction synchronization support. Default is "always".
     * <p>Note that transaction synchronization isn't supported for
     * multiple concurrent transactions by different transaction managers.
     * Only one transaction manager is allowed to activate it at any time.
     * @see #SYNCHRONIZATION_ALWAYS
     * @see #SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
     * @see #SYNCHRONIZATION_NEVER
     * @see TransactionSynchronizationManager
     * @see TransactionSynchronization
     */
    public final void setTransactionSynchronization(int transactionSynchronization) {
        this.transactionSynchronization = transactionSynchronization;
    }

    /**
     * Return if this transaction manager should activate the thread-bound
     * transaction synchronization support.
     */
    public final int getTransactionSynchronization() {
        return this.transactionSynchronization;
    }

    /**
     * Specify the default timeout that this transaction manager should apply
     * if there is no timeout specified at the transaction level, in seconds.
     * <p>Default is the underlying transaction infrastructure's default timeout,
     * e.g. typically 30 seconds in case of a JTA provider, indicated by the
     * {@code TransactionDefinition.TIMEOUT_DEFAULT} value.
     * @see TransactionDefinition#TIMEOUT_DEFAULT
     */
    public final void setDefaultTimeout(int defaultTimeout) {
        if (defaultTimeout < TransactionDefinition.TIMEOUT_DEFAULT) {
            throw new InvalidTimeoutException("Invalid default timeout", defaultTimeout);
        }
        this.defaultTimeout = defaultTimeout;
    }

    /**
     * Return the default timeout that this transaction manager should apply
     * if there is no timeout specified at the transaction level, in seconds.
     * <p>Returns {@code TransactionDefinition.TIMEOUT_DEFAULT} to indicate
     * the underlying transaction infrastructure's default timeout.
     */
    public final int getDefaultTimeout() {
        return this.defaultTimeout;
    }

    /**
     * Set whether nested transactions are allowed. Default is "false".
     * <p>Typically initialized with an appropriate default by the
     * concrete transaction manager subclass.
     */
    public final void setNestedTransactionAllowed(boolean nestedTransactionAllowed) {
        this.nestedTransactionAllowed = nestedTransactionAllowed;
    }

    /**
     * Return whether nested transactions are allowed.
     */
    public final boolean isNestedTransactionAllowed() {
        return this.nestedTransactionAllowed;
    }

    /**
     * Set whether existing transactions should be validated before participating
     * in them.
     * <p>When participating in an existing transaction (e.g. with
     * PROPAGATION_REQUIRES or PROPAGATION_SUPPORTS encountering an existing
     * transaction), this outer transaction's characteristics will apply even
     * to the inner transaction scope. Validation will detect incompatible
     * isolation level and read-only settings on the inner transaction definition
     * and reject participation accordingly through throwing a corresponding exception.
     * <p>Default is "false", leniently ignoring inner transaction settings,
     * simply overriding them with the outer transaction's characteristics.
     * Switch this flag to "true" in order to enforce strict validation.
     */
    public final void setValidateExistingTransaction(boolean validateExistingTransaction) {
        this.validateExistingTransaction = validateExistingTransaction;
    }

    /**
     * Return whether existing transactions should be validated before participating
     * in them.
     */
    public final boolean isValidateExistingTransaction() {
        return this.validateExistingTransaction;
    }

    /**
     * Set whether to globally mark an existing transaction as rollback-only
     * after a participating transaction failed.
     * <p>Default is "true": If a participating transaction (e.g. with
     * PROPAGATION_REQUIRES or PROPAGATION_SUPPORTS encountering an existing
     * transaction) fails, the transaction will be globally marked as rollback-only.
     * The only possible outcome of such a transaction is a rollback: The
     * transaction originator <i>cannot</i> make the transaction commit anymore.
     * <p>Switch this to "false" to let the transaction originator make the rollback
     * decision. If a participating transaction fails with an exception, the caller
     * can still decide to continue with a different path within the transaction.
     * However, note that this will only work as long as all participating resources
     * are capable of continuing towards a transaction commit even after a data access
     * failure: This is generally not the case for a Hibernate Session, for example;
     * neither is it for a sequence of JDBC insert/update/delete operations.
     * <p><b>Note:</b>This flag only applies to an explicit rollback attempt for a
     * subtransaction, typically caused by an exception thrown by a data access operation
     * (where TransactionInterceptor will trigger a {@code PlatformTransactionManager.rollback()}
     * call according to a rollback rule). If the flag is off, the caller can handle the exception
     * and decide on a rollback, independent of the rollback rules of the subtransaction.
     * This flag does, however, <i>not</i> apply to explicit {@code setRollbackOnly}
     * calls on a {@code TransactionStatus}, which will always cause an eventual
     * global rollback (as it might not throw an exception after the rollback-only call).
     * <p>The recommended solution for handling failure of a subtransaction
     * is a "nested transaction", where the global transaction can be rolled
     * back to a savepoint taken at the beginning of the subtransaction.
     * PROPAGATION_NESTED provides exactly those semantics; however, it will
     * only work when nested transaction support is available. This is the case
     * with DataSourceTransactionManager, but not with JtaTransactionManager.
     * @see #setNestedTransactionAllowed
     * @see org.springframework.transaction.jta.JtaTransactionManager
     */
    public final void setGlobalRollbackOnParticipationFailure(boolean globalRollbackOnParticipationFailure) {
        this.globalRollbackOnParticipationFailure = globalRollbackOnParticipationFailure;
    }

    /**
     * Return whether to globally mark an existing transaction as rollback-only
     * after a participating transaction failed.
     */
    public final boolean isGlobalRollbackOnParticipationFailure() {
        return this.globalRollbackOnParticipationFailure;
    }

    /**
     * Set whether to fail early in case of the transaction being globally marked
     * as rollback-only.
     * <p>Default is "false", only causing an UnexpectedRollbackException at the
     * outermost transaction boundary. Switch this flag on to cause an
     * UnexpectedRollbackException as early as the global rollback-only marker
     * has been first detected, even from within an inner transaction boundary.
     * <p>Note that, as of Spring 2.0, the fail-early behavior for global
     * rollback-only markers has been unified: All transaction managers will by
     * default only cause UnexpectedRollbackException at the outermost transaction
     * boundary. This allows, for example, to continue unit tests even after an
     * operation failed and the transaction will never be completed. All transaction
     * managers will only fail earlier if this flag has explicitly been set to "true".
     * @see UnexpectedRollbackException
     */
    public final void setFailEarlyOnGlobalRollbackOnly(boolean failEarlyOnGlobalRollbackOnly) {
        this.failEarlyOnGlobalRollbackOnly = failEarlyOnGlobalRollbackOnly;
    }

    /**
     * Return whether to fail early in case of the transaction being globally marked
     * as rollback-only.
     */
    public final boolean isFailEarlyOnGlobalRollbackOnly() {
        return this.failEarlyOnGlobalRollbackOnly;
    }

    /**
     * Set whether {@code doRollback} should be performed on failure of the
     * {@code doCommit} call. Typically not necessary and thus to be avoided,
     * as it can potentially override the commit exception with a subsequent
     * rollback exception.
     * <p>Default is "false".
     * @see #doCommit
     * @see #doRollback
     */
    public final void setRollbackOnCommitFailure(boolean rollbackOnCommitFailure) {
        this.rollbackOnCommitFailure = rollbackOnCommitFailure;
    }

    /**
     * Return whether {@code doRollback} should be performed on failure of the
     * {@code doCommit} call.
     */
    public final boolean isRollbackOnCommitFailure() {
        return this.rollbackOnCommitFailure;
    }


    //---------------------------------------------------------------------
    // Implementation of PlatformTransactionManager
    //---------------------------------------------------------------------

    /**
     * This implementation handles propagation behavior. Delegates to
     * {@code doGetTransaction}, {@code isExistingTransaction}
     * and {@code doBegin}.
     * @see #doGetTransaction
     * @see #isExistingTransaction
     * @see #doBegin
     */
    public final TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {

        //doGetTransaction()方法是抽象方法，具体的实现由具体的事务处理器提供
        Object transaction = doGetTransaction();

        // Cache debug flag to avoid repeated checks.
        boolean debugEnabled = logger.isDebugEnabled();

        //如果没有配置事务属性，则使用默认的事务属性
        if (definition == null) {
            // 使用默认的事务属性
            definition = new DefaultTransactionDefinition();
        }

        //检查当前线程是否存在事务
        if (isExistingTransaction(transaction)) {
            //处理已存在的事务
            return handleExistingTransaction(definition, transaction, debugEnabled);
        }

        //检查事务属性中timeout超时属性设置是否合理
        if (definition.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
            throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
        }

        //对事务属性中配置的事务传播特性处理
        //如果事务传播特性配置的是mandatory，当前没有事务存在，抛出异常
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY) {
            throw new IllegalTransactionStateException(
                    "No existing transaction found for transaction marked with propagation 'mandatory'");
        }
        //如果事务传播特性为required、required_new或nested
        else if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
                definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
                definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
            SuspendedResourcesHolder suspendedResources = suspend(null);
            if (debugEnabled) {
                logger.debug("Creating new transaction with name [" + definition.getName() + "]: " + definition);
            }
            //创建事务
            try {
                //不激活和当前线程绑定的事务，因为事务传播特性配置要求创建新的事务
                boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
                //创建一个新的事务状态
                DefaultTransactionStatus status = newTransactionStatus(
                        definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
                //创建事务的调用，具体实现由具体的事务处理器提供
                doBegin(transaction, definition);
                //初始化和同步事务状态
                prepareSynchronization(status, definition);
                return status;
            }
            catch (RuntimeException ex) {
                resume(null, suspendedResources);
                throw ex;
            }
            catch (Error err) {
                resume(null, suspendedResources);
                throw err;
            }
        }
        else {
            //创建空事务，针对supported类型的事务传播特性，激活和当前线程绑定的事务
            boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
            return prepareTransactionStatus(definition, null, true, newSynchronization, debugEnabled, null);
        }
    }

    /**
     * Create a TransactionStatus for an existing transaction.
     */
    private TransactionStatus handleExistingTransaction(
            TransactionDefinition definition, Object transaction, boolean debugEnabled)
            throws TransactionException {

        boolean definitionReadOnly = definition.isReadOnly();
        boolean isCurrentReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if(definitionReadOnly != isCurrentReadOnly || DataSourceProxyManager.hasSuspendedTransactionActive()){
            logger.info("当前事务属性与前一个事务属性不同了。");
            SuspendedResourcesHolder suspendedResources = suspend(transaction);
            try {
                boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
                DefaultTransactionStatus status = newTransactionStatus(
                        definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
                doBegin(transaction, definition);
                prepareSynchronization(status, definition);
                return status;
            }
            catch (RuntimeException beginEx) {
                resumeAfterBeginException(transaction, suspendedResources, beginEx);
                throw beginEx;
            }
            catch (Error beginErr) {
                resumeAfterBeginException(transaction, suspendedResources, beginErr);
                throw beginErr;
            }
        }
        //如果事务传播特性为：never，则抛出异常
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NEVER) {
            throw new IllegalTransactionStateException(
                    "Existing transaction found for transaction marked with propagation 'never'");
        }
        //如果事务传播特性是not_supported，同时当前线程存在事务，则将事务挂起
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NOT_SUPPORTED) {
            if (debugEnabled) {
                logger.debug("Suspending current transaction");
            }
            //挂起事务
            Object suspendedResources = suspend(transaction);
            boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
            //创建非事务的事务状态，让方法非事务地执行
            return prepareTransactionStatus(
                    definition, null, false, newSynchronization, debugEnabled, suspendedResources);
        }
        //如果事务传播特性是required_new，则创建新事务，同时把当前线程中存在的事务挂起
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW) {
            if (debugEnabled) {
                logger.debug("Suspending current transaction, creating new transaction with name [" +
                        definition.getName() + "]");
            }
            //挂起已存在的事务
            SuspendedResourcesHolder suspendedResources = suspend(transaction);
            try {
                boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
                //将挂起的事务状态保存起来
                DefaultTransactionStatus status = newTransactionStatus(
                        definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
                //创建新事务
                doBegin(transaction, definition);
                prepareSynchronization(status, definition);
                return status;
            }
            catch (RuntimeException beginEx) {
                resumeAfterBeginException(transaction, suspendedResources, beginEx);
                throw beginEx;
            }
            catch (Error beginErr) {
                resumeAfterBeginException(transaction, suspendedResources, beginErr);
                throw beginErr;
            }
        }
        //如果事务传播特性是nested嵌套事务
        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
            //如果不允许事务嵌套，则抛出异常
            if (!isNestedTransactionAllowed()) {
                throw new NestedTransactionNotSupportedException(
                        "Transaction manager does not allow nested transactions by default - " +
                                "specify 'nestedTransactionAllowed' property with value 'true'");
            }
            if (debugEnabled) {
                logger.debug("Creating nested transaction with name [" + definition.getName() + "]");
            }
            //如果允许使用savepoint保存点保存嵌套事务
            if (useSavepointForNestedTransaction()) {
                // Create savepoint within existing Spring-managed transaction,
                // through the SavepointManager API implemented by TransactionStatus.
                // Usually uses JDBC 3.0 savepoints. Never activates Spring synchronization.
                //为当前事务创建一个保存点
                DefaultTransactionStatus status =
                        prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
                status.createAndHoldSavepoint();
                return status;
            }
            //如果不允许使用savepoint保存点保存嵌套事务
            else {
                // Nested transaction through nested begin and commit/rollback calls.
                // Usually only for JTA: Spring synchronization might get activated here
                // in case of a pre-existing JTA transaction.
                //使用JTA的嵌套commit/rollback调用
                boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
                DefaultTransactionStatus status = newTransactionStatus(
                        definition, transaction, true, newSynchronization, debugEnabled, null);
                doBegin(transaction, definition);
                prepareSynchronization(status, definition);
                return status;
            }
        }

        //对于事务传播特性为supported和required的处理
        if (debugEnabled) {
            logger.debug("Participating in existing transaction");
        }
        boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
        return prepareTransactionStatus(definition, transaction, false, newSynchronization, debugEnabled, null);
    }

    /**
     * Create a new TransactionStatus for the given arguments,
     * also initializing transaction synchronization as appropriate.
     * 准备事务状态
     * @see #newTransactionStatus
     * @see #prepareTransactionStatus
     */
    protected final DefaultTransactionStatus prepareTransactionStatus(
            TransactionDefinition definition, Object transaction, boolean newTransaction,
            boolean newSynchronization, boolean debug, Object suspendedResources) {
        //创建事务状态
        DefaultTransactionStatus status = newTransactionStatus(
                definition, transaction, newTransaction, newSynchronization, debug, suspendedResources);
        //准备事务状态
        prepareSynchronization(status, definition);
        return status;
    }

    /**
     * 创建事务状态
     */
    protected DefaultTransactionStatus newTransactionStatus(
            TransactionDefinition definition, Object transaction, boolean newTransaction,
            boolean newSynchronization, boolean debug, Object suspendedResources) {
        //判断是否是新事务，如果是新事务，则需要把事务属性存放到当前线程中
        boolean actualNewSynchronization = newSynchronization &&
                !TransactionSynchronizationManager.isSynchronizationActive();
        return new DefaultTransactionStatus(
                transaction, newTransaction, actualNewSynchronization,
                definition.isReadOnly(), debug, suspendedResources);
    }

    /**
     * //初始化事务属性
     */
    protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
        if (status.isNewSynchronization()) {

            //设置当前是否有活跃事务
            TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());


            //设置当前事务隔离级别
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
                    (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) ?
                            definition.getIsolationLevel() : null);

            TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());

            //设置当前事务名称
            TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());

            TransactionSynchronizationManager.initSynchronization();
        }
    }

    /**
     * Determine the actual timeout to use for the given definition.
     * Will fall back to this manager's default timeout if the
     * transaction definition doesn't specify a non-default value.
     * @param definition the transaction definition
     * @return the actual timeout to use
     * @see TransactionDefinition#getTimeout()
     * @see #setDefaultTimeout
     */
    protected int determineTimeout(TransactionDefinition definition) {
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
            return definition.getTimeout();
        }
        return this.defaultTimeout;
    }


    /**
     * Suspend the given transaction. Suspends transaction synchronization first,
     * then delegates to the {@code doSuspend} template method.
     * @param transaction the current transaction object
     * (or {@code null} to just suspend active synchronizations, if any)
     * @return an object that holds suspended resources
     * (or {@code null} if neither transaction nor synchronization active)
     * @see #doSuspend
     * @see #resume
     */
    protected final SuspendedResourcesHolder suspend(Object transaction) throws TransactionException {
        //如果事务是激活的，且当前线程事务同步机制也是激活状态
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            //挂起当前线程中所有同步的事务
            List<TransactionSynchronization> suspendedSynchronizations = doSuspendSynchronization();
            try {
                Object suspendedResources = null;
                //把挂起事务的操作交由具体的事务处理器处理
                if (transaction != null) {
                    suspendedResources = doSuspend(transaction);
                }
                //在线程中保存与事务处理有关的信息，并将线程里有关的线程局部变量重置
                String name = TransactionSynchronizationManager.getCurrentTransactionName();
                //重置当前线程中事务相关的线程局部变量
                TransactionSynchronizationManager.setCurrentTransactionName(null);
                boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
                TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
                Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
                TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(null);
                boolean wasActive = TransactionSynchronizationManager.isActualTransactionActive();
                TransactionSynchronizationManager.setActualTransactionActive(false);
                //将当前线程中事务相关信息保存
                return new SuspendedResourcesHolder(
                        suspendedResources, suspendedSynchronizations, name, readOnly, isolationLevel, wasActive);
            }
            //对事务挂起操作中产生异常和错误的处理
            catch (RuntimeException ex) {
                // doSuspend failed - original transaction is still active...
                doResumeSynchronization(suspendedSynchronizations);
                throw ex;
            }
            catch (Error err) {
                // doSuspend failed - original transaction is still active...
                doResumeSynchronization(suspendedSynchronizations);
                throw err;
            }
        }
        //如果事务是激活的，但是事务同步机制不是激活的，则只需要保存事务状态，不
        //需要重置事务相关的线程局部变量
        else if (transaction != null) {
            // Transaction active but no synchronization active.
            Object suspendedResources = doSuspend(transaction);
            return new SuspendedResourcesHolder(suspendedResources);
        }
        //事务和事务同步机制都不是激活的，则不要想处理
        else {
            // Neither transaction nor synchronization active.
            return null;
        }
    }

    /**
     * Resume the given transaction. Delegates to the {@code doResume}
     * template method first, then resuming transaction synchronization.
     * @param transaction the current transaction object
     * @param resourcesHolder the object that holds suspended resources,
     * as returned by {@code suspend} (or {@code null} to just
     * resume synchronizations, if any)
     * @see #doResume
     * @see #suspend
     */
    protected final void resume(Object transaction, SuspendedResourcesHolder resourcesHolder)
            throws TransactionException {

        if (resourcesHolder != null) {
            Object suspendedResources = resourcesHolder.suspendedResources;
            if (suspendedResources != null) {
                doResume(transaction, suspendedResources);
            }
            List<TransactionSynchronization> suspendedSynchronizations = resourcesHolder.suspendedSynchronizations;
            if (suspendedSynchronizations != null) {
                TransactionSynchronizationManager.setActualTransactionActive(resourcesHolder.wasActive);
                TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(resourcesHolder.isolationLevel);
                TransactionSynchronizationManager.setCurrentTransactionReadOnly(resourcesHolder.readOnly);
                TransactionSynchronizationManager.setCurrentTransactionName(resourcesHolder.name);
                doResumeSynchronization(suspendedSynchronizations);
            }
        }
    }

    /**
     * Resume outer transaction after inner transaction begin failed.
     */
    private void resumeAfterBeginException(
            Object transaction, SuspendedResourcesHolder suspendedResources, Throwable beginEx) {

        String exMessage = "Inner transaction begin exception overridden by outer transaction resume exception";
        try {
            resume(transaction, suspendedResources);
        }
        catch (RuntimeException resumeEx) {
            logger.error(exMessage, beginEx);
            throw resumeEx;
        }
        catch (Error resumeErr) {
            logger.error(exMessage, beginEx);
            throw resumeErr;
        }
    }

    /**
     * Suspend all current synchronizations and deactivate transaction
     * synchronization for the current thread.
     * @return the List of suspended TransactionSynchronization objects
     */
    private List<TransactionSynchronization> doSuspendSynchronization() {
        List<TransactionSynchronization> suspendedSynchronizations =
                TransactionSynchronizationManager.getSynchronizations();
        for (TransactionSynchronization synchronization : suspendedSynchronizations) {
            synchronization.suspend();
        }
        TransactionSynchronizationManager.clearSynchronization();
        return suspendedSynchronizations;
    }

    /**
     * Reactivate transaction synchronization for the current thread
     * and resume all given synchronizations.
     * @param suspendedSynchronizations List of TransactionSynchronization objects
     */
    private void doResumeSynchronization(List<TransactionSynchronization> suspendedSynchronizations) {
        TransactionSynchronizationManager.initSynchronization();
        for (TransactionSynchronization synchronization : suspendedSynchronizations) {
            synchronization.resume();
            TransactionSynchronizationManager.registerSynchronization(synchronization);
        }
    }


    /**
     * This implementation of commit handles participating in existing
     * transactions and programmatic rollback requests.
     * Delegates to {@code isRollbackOnly}, {@code doCommit}
     * and {@code rollback}.
     * @see TransactionStatus#isRollbackOnly()
     * @see #doCommit
     * @see #rollback
     */
    public final void commit(TransactionStatus status) throws TransactionException {
        //如果事务的执行状态已经结束，则抛出异常
        if (status.isCompleted()) {
            throw new IllegalTransactionStateException(
                    "Transaction is already completed - do not call commit or rollback more than once per transaction");
        }

        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        //如果事务执行状态时回滚
        if (defStatus.isLocalRollbackOnly()) {
            if (defStatus.isDebug()) {
                logger.debug("Transactional code has requested rollback");
            }
            //处理事务回滚
            processRollback(defStatus);
            return;
        }
        //如果事务没有被标记为回滚时提交，且事务状态时全局回滚
        if (!shouldCommitOnGlobalRollbackOnly() && defStatus.isGlobalRollbackOnly()) {
            if (defStatus.isDebug()) {
                logger.debug("Global transaction is marked as rollback-only but transactional code requested commit");
            }
            //回滚处理
            processRollback(defStatus);
            // Throw UnexpectedRollbackException only at outermost transaction boundary
            // or if explicitly asked to.
            //如果事务状态时新事务，或者在全局回滚时失败
            if (status.isNewTransaction() || isFailEarlyOnGlobalRollbackOnly()) {
                throw new UnexpectedRollbackException(
                        "Transaction rolled back because it has been marked as rollback-only");
            }
            return;
        }
        //处理提交
        processCommit(defStatus);
    }

    /**
     * Process an actual commit.
     * Rollback-only flags have already been checked and applied.
     * @param status object representing the transaction
     * @throws TransactionException in case of commit failure
     */
    private void processCommit(DefaultTransactionStatus status) throws TransactionException {
        try {
            boolean beforeCompletionInvoked = false;
            try {
                //事务提交的准备工作，有具体的事务处理器完成
                prepareForCommit(status);
                triggerBeforeCommit(status);
                triggerBeforeCompletion(status);
                beforeCompletionInvoked = true;
                boolean globalRollbackOnly = false;
                //如果事务状态是新事务，或者全局回滚失败
                if (status.isNewTransaction() || isFailEarlyOnGlobalRollbackOnly()) {
                    //设置事务全局回滚
                    globalRollbackOnly = status.isGlobalRollbackOnly();
                }
                //嵌套事务处理
                if (status.hasSavepoint()) {
                    if (status.isDebug()) {
                        logger.debug("Releasing transaction savepoint");
                    }
                    //释放挂起事务保存点
                    status.releaseHeldSavepoint();
                }
                //如果当前事务是新事务
                else if (status.isNewTransaction()) {
                    if (status.isDebug()) {
                        logger.debug("Initiating transaction commit");
                    }
                    //调用具体事务处理器提交事务
                    doCommit(status);
                }
                // Throw UnexpectedRollbackException if we have a global rollback-only
                // marker but still didn't get a corresponding exception from commit.
                //如果事务被标记为全局回滚
                if (globalRollbackOnly) {
                    throw new UnexpectedRollbackException(
                            "Transaction silently rolled back because it has been marked as rollback-only");
                }
            }
            //提交过程中产生未预期的回滚异常，则回滚处理
            catch (UnexpectedRollbackException ex) {
                // can only be caused by doCommit
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);
                throw ex;
            }
            //对提交过程中产生的事务异常处理
            catch (TransactionException ex) {
                // can only be caused by doCommit
                //如果回滚失败，则进行回滚异常处理
                if (isRollbackOnCommitFailure()) {
                    doRollbackOnCommitException(status, ex);
                }
                else {
                    triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
                }
                throw ex;
            }
            //对提交过程中产生的异常处理
            catch (RuntimeException ex) {
                //如果不是在完成前调用的
                if (!beforeCompletionInvoked) {
                    //触发完成前的回调方法
                    triggerBeforeCompletion(status);
                }
                //进行回滚异常处理
                doRollbackOnCommitException(status, ex);
                throw ex;
            }
            //对提交过程中产生的错误处理
            catch (Error err) {
                if (!beforeCompletionInvoked) {
                    triggerBeforeCompletion(status);
                }
                doRollbackOnCommitException(status, err);
                throw err;
            }

            // Trigger afterCommit callbacks, with an exception thrown there
            // propagated to callers but the transaction still considered as committed.
            //触发提交之后的回调操作
            try {
                triggerAfterCommit(status);
            }
            //提交完成之后清除事务相关状态
            finally {
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_COMMITTED);
            }

        }
        finally {
            cleanupAfterCompletion(status);
        }
    }

    /**
     * This implementation of rollback handles participating in existing
     * transactions. Delegates to {@code doRollback} and
     * {@code doSetRollbackOnly}.
     * @see #doRollback
     * @see #doSetRollbackOnly
     */
    public final void rollback(TransactionStatus status) throws TransactionException {
        //如果事务状态已完成，则抛出异常
        if (status.isCompleted()) {
            throw new IllegalTransactionStateException(
                    "Transaction is already completed - do not call commit or rollback more than once per transaction");
        }

        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        //处理回滚的操作
        processRollback(defStatus);
    }

    /**
     * Process an actual rollback.
     * The completed flag has already been checked.
     * 回滚操作
     * @param status object representing the transaction
     * @throws TransactionException in case of rollback failure
     */
    private void processRollback(DefaultTransactionStatus status) {
        try {
            try {
                //触发完成前的回调操作
                triggerBeforeCompletion(status);
                //嵌套事务回滚处理
                if (status.hasSavepoint()) {
                    if (status.isDebug()) {
                        logger.debug("Rolling back transaction to savepoint");
                    }
                    //回滚挂起在保存点的事务
                    status.rollbackToHeldSavepoint();
                }
                //当前事务中创建新事务的回滚操作
                else if (status.isNewTransaction()) {
                    if (status.isDebug()) {
                        logger.debug("Initiating transaction rollback");
                    }
                    //回滚处理，由具体的事务处理器实现
                    doRollback(status);
                }
                //如果在当前事务中没有新建事务
                else if (status.hasTransaction()) {
                    //如果当前事务状态为本地回滚，或全局回滚失败
                    if (status.isLocalRollbackOnly() || isGlobalRollbackOnParticipationFailure()) {
                        if (status.isDebug()) {
                            logger.debug("Participating transaction failed - marking existing transaction as rollback-only");
                        }
                        //设置当前事务状态为回滚
                        doSetRollbackOnly(status);
                    }
                    //当前事务状态没有设置为本地回滚，且没有产生全局回滚失败，则
                    //由线程中的前一个事务来处理回滚，这个步骤任何处理
                    else {
                        if (status.isDebug()) {
                            logger.debug("Participating transaction failed - letting transaction originator decide on rollback");
                        }
                    }
                }
                //如果当前线程没有事务
                else {
                    logger.debug("Should roll back transaction but cannot - no transaction available");
                }
            }
            //对回滚操作过程中的运行时异常和错误的处理
            catch (RuntimeException ex) {
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
                throw ex;
            }
            catch (Error err) {
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
                throw err;
            }
            //回滚操作完成后，触发回滚之后回调操作
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);
        }
        //清除回滚之后事务状态信息
        finally {
            cleanupAfterCompletion(status);
        }
    }

    /**
     * Invoke {@code doRollback}, handling rollback exceptions properly.
     * @param status object representing the transaction
     * @param ex the thrown application exception or error
     * @throws TransactionException in case of rollback failure
     * @see #doRollback
     */
    private void doRollbackOnCommitException(DefaultTransactionStatus status, Throwable ex) throws TransactionException {
        try {
            if (status.isNewTransaction()) {
                if (status.isDebug()) {
                    logger.debug("Initiating transaction rollback after commit exception", ex);
                }
                doRollback(status);
            }
            else if (status.hasTransaction() && isGlobalRollbackOnParticipationFailure()) {
                if (status.isDebug()) {
                    logger.debug("Marking existing transaction as rollback-only after commit exception", ex);
                }
                doSetRollbackOnly(status);
            }
        }
        catch (RuntimeException rbex) {
            logger.error("Commit exception overridden by rollback exception", ex);
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
            throw rbex;
        }
        catch (Error rberr) {
            logger.error("Commit exception overridden by rollback exception", ex);
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
            throw rberr;
        }
        triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);
    }


    /**
     * Trigger {@code beforeCommit} callbacks.
     * @param status object representing the transaction
     */
    protected final void triggerBeforeCommit(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                logger.trace("Triggering beforeCommit synchronization");
            }
            TransactionSynchronizationUtils.triggerBeforeCommit(status.isReadOnly());
        }
    }

    /**
     * Trigger {@code beforeCompletion} callbacks.
     * @param status object representing the transaction
     */
    protected final void triggerBeforeCompletion(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                logger.trace("Triggering beforeCompletion synchronization");
            }
            TransactionSynchronizationUtils.triggerBeforeCompletion();
        }
    }

    /**
     * Trigger {@code afterCommit} callbacks.
     * @param status object representing the transaction
     */
    private void triggerAfterCommit(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                logger.trace("Triggering afterCommit synchronization");
            }
            TransactionSynchronizationUtils.triggerAfterCommit();
        }
    }

    /**
     * Trigger {@code afterCompletion} callbacks.
     * @param status object representing the transaction
     * @param completionStatus completion status according to TransactionSynchronization constants
     */
    private void triggerAfterCompletion(DefaultTransactionStatus status, int completionStatus) {
        if (status.isNewSynchronization()) {
            List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
            if (!status.hasTransaction() || status.isNewTransaction()) {
                if (status.isDebug()) {
                    logger.trace("Triggering afterCompletion synchronization");
                }
                // No transaction or new transaction for the current scope ->
                // invoke the afterCompletion callbacks immediately
                invokeAfterCompletion(synchronizations, completionStatus);
            }
            else if (!synchronizations.isEmpty()) {
                // Existing transaction that we participate in, controlled outside
                // of the scope of this Spring transaction manager -> try to register
                // an afterCompletion callback with the existing (JTA) transaction.
                registerAfterCompletionWithExistingTransaction(status.getTransaction(), synchronizations);
            }
        }
    }

    /**
     * Actually invoke the {@code afterCompletion} methods of the
     * given Spring TransactionSynchronization objects.
     * <p>To be called by this abstract manager itself, or by special implementations
     * of the {@code registerAfterCompletionWithExistingTransaction} callback.
     * @param synchronizations List of TransactionSynchronization objects
     * @param completionStatus the completion status according to the
     * constants in the TransactionSynchronization interface
     * @see #registerAfterCompletionWithExistingTransaction(Object, List)
     * @see TransactionSynchronization#STATUS_COMMITTED
     * @see TransactionSynchronization#STATUS_ROLLED_BACK
     * @see TransactionSynchronization#STATUS_UNKNOWN
     */
    protected final void invokeAfterCompletion(List<TransactionSynchronization> synchronizations, int completionStatus) {
        TransactionSynchronizationUtils.invokeAfterCompletion(synchronizations, completionStatus);
    }

    /**
     * Clean up after completion, clearing synchronization if necessary,
     * and invoking doCleanupAfterCompletion.
     * @param status object representing the transaction
     * @see #doCleanupAfterCompletion
     */
    private void cleanupAfterCompletion(DefaultTransactionStatus status) {
        status.setCompleted();
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.clear();
        }
        if (status.isNewTransaction()) {
            doCleanupAfterCompletion(status.getTransaction());
        }
        if (status.getSuspendedResources() != null) {
            if (status.isDebug()) {
                logger.debug("Resuming suspended transaction after completion of inner transaction");
            }
            resume(status.getTransaction(), (SuspendedResourcesHolder) status.getSuspendedResources());
        }
    }


    //---------------------------------------------------------------------
    // Template methods to be implemented in subclasses
    //---------------------------------------------------------------------

    /**
     * Return a transaction object for the current transaction state.
     * <p>The returned object will usually be specific to the concrete transaction
     * manager implementation, carrying corresponding transaction state in a
     * modifiable fashion. This object will be passed into the other template
     * methods (e.g. doBegin and doCommit), either directly or as part of a
     * DefaultTransactionStatus instance.
     * <p>The returned object should contain information about any existing
     * transaction, that is, a transaction that has already started before the
     * current {@code getTransaction} call on the transaction manager.
     * Consequently, a {@code doGetTransaction} implementation will usually
     * look for an existing transaction and store corresponding state in the
     * returned transaction object.
     * @return the current transaction object
     * @throws CannotCreateTransactionException
     * if transaction support is not available
     * @throws TransactionException in case of lookup or system errors
     * @see #doBegin
     * @see #doCommit
     * @see #doRollback
     * @see DefaultTransactionStatus#getTransaction
     */
    protected abstract Object doGetTransaction() throws TransactionException;

    /**
     * Check if the given transaction object indicates an existing transaction
     * (that is, a transaction which has already started).
     * <p>The result will be evaluated according to the specified propagation
     * behavior for the new transaction. An existing transaction might get
     * suspended (in case of PROPAGATION_REQUIRES_NEW), or the new transaction
     * might participate in the existing one (in case of PROPAGATION_REQUIRED).
     * <p>The default implementation returns {@code false}, assuming that
     * participating in existing transactions is generally not supported.
     * Subclasses are of course encouraged to provide such support.
     * @param transaction transaction object returned by doGetTransaction
     * @return if there is an existing transaction
     * @throws TransactionException in case of system errors
     * @see #doGetTransaction
     */
    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        return false;
    }

    /**
     * Return whether to use a savepoint for a nested transaction.
     * <p>Default is {@code true}, which causes delegation to DefaultTransactionStatus
     * for creating and holding a savepoint. If the transaction object does not implement
     * the SavepointManager interface, a NestedTransactionNotSupportedException will be
     * thrown. Else, the SavepointManager will be asked to create a new savepoint to
     * demarcate the start of the nested transaction.
     * <p>Subclasses can override this to return {@code false}, causing a further
     * call to {@code doBegin} - within the context of an already existing transaction.
     * The {@code doBegin} implementation needs to handle this accordingly in such
     * a scenario. This is appropriate for JTA, for example.
     * @see DefaultTransactionStatus#createAndHoldSavepoint
     * @see DefaultTransactionStatus#rollbackToHeldSavepoint
     * @see DefaultTransactionStatus#releaseHeldSavepoint
     * @see #doBegin
     */
    protected boolean useSavepointForNestedTransaction() {
        return true;
    }

    /**
     * Begin a new transaction with semantics according to the given transaction
     * definition. Does not have to care about applying the propagation behavior,
     * as this has already been handled by this abstract manager.
     * <p>This method gets called when the transaction manager has decided to actually
     * start a new transaction. Either there wasn't any transaction before, or the
     * previous transaction has been suspended.
     * <p>A special scenario is a nested transaction without savepoint: If
     * {@code useSavepointForNestedTransaction()} returns "false", this method
     * will be called to start a nested transaction when necessary. In such a context,
     * there will be an active transaction: The implementation of this method has
     * to detect this and start an appropriate nested transaction.
     * @param transaction transaction object returned by {@code doGetTransaction}
     * @param definition TransactionDefinition instance, describing propagation
     * behavior, isolation level, read-only flag, timeout, and transaction name
     * @throws TransactionException in case of creation or system errors
     */
    protected abstract void doBegin(Object transaction, TransactionDefinition definition)
            throws TransactionException;

    /**
     * Suspend the resources of the current transaction.
     * Transaction synchronization will already have been suspended.
     * <p>The default implementation throws a TransactionSuspensionNotSupportedException,
     * assuming that transaction suspension is generally not supported.
     * @param transaction transaction object returned by {@code doGetTransaction}
     * @return an object that holds suspended resources
     * (will be kept unexamined for passing it into doResume)
     * @throws TransactionSuspensionNotSupportedException
     * if suspending is not supported by the transaction manager implementation
     * @throws TransactionException in case of system errors
     * @see #doResume
     */
    protected Object doSuspend(Object transaction) throws TransactionException {
        throw new TransactionSuspensionNotSupportedException(
                "Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
    }

    /**
     * Resume the resources of the current transaction.
     * Transaction synchronization will be resumed afterwards.
     * <p>The default implementation throws a TransactionSuspensionNotSupportedException,
     * assuming that transaction suspension is generally not supported.
     * @param transaction transaction object returned by {@code doGetTransaction}
     * @param suspendedResources the object that holds suspended resources,
     * as returned by doSuspend
     * @throws TransactionSuspensionNotSupportedException
     * if resuming is not supported by the transaction manager implementation
     * @throws TransactionException in case of system errors
     * @see #doSuspend
     */
    protected void doResume(Object transaction, Object suspendedResources) throws TransactionException {
        throw new TransactionSuspensionNotSupportedException(
                "Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
    }

    /**
     * Return whether to call {@code doCommit} on a transaction that has been
     * marked as rollback-only in a global fashion.
     * <p>Does not apply if an application locally sets the transaction to rollback-only
     * via the TransactionStatus, but only to the transaction itself being marked as
     * rollback-only by the transaction coordinator.
     * <p>Default is "false": Local transaction strategies usually don't hold the rollback-only
     * marker in the transaction itself, therefore they can't handle rollback-only transactions
     * as part of transaction commit. Hence, AbstractPlatformTransactionManager will trigger
     * a rollback in that case, throwing an UnexpectedRollbackException afterwards.
     * <p>Override this to return "true" if the concrete transaction manager expects a
     * {@code doCommit} call even for a rollback-only transaction, allowing for
     * special handling there. This will, for example, be the case for JTA, where
     * {@code UserTransaction.commit} will check the read-only flag itself and
     * throw a corresponding RollbackException, which might include the specific reason
     * (such as a transaction timeout).
     * <p>If this method returns "true" but the {@code doCommit} implementation does not
     * throw an exception, this transaction manager will throw an UnexpectedRollbackException
     * itself. This should not be the typical case; it is mainly checked to cover misbehaving
     * JTA providers that silently roll back even when the rollback has not been requested
     * by the calling code.
     * @see #doCommit
     * @see DefaultTransactionStatus#isGlobalRollbackOnly()
     * @see DefaultTransactionStatus#isLocalRollbackOnly()
     * @see TransactionStatus#setRollbackOnly()
     * @see UnexpectedRollbackException
     * ///@see javax.transaction.UserTransaction#commit()
     * //@see javax.transaction.RollbackException
     */
    protected boolean shouldCommitOnGlobalRollbackOnly() {
        return false;
    }

    /**
     * Make preparations for commit, to be performed before the
     * {@code beforeCommit} synchronization callbacks occur.
     * <p>Note that exceptions will get propagated to the commit caller
     * and cause a rollback of the transaction.
     * @param status the status representation of the transaction
     * @throws RuntimeException in case of errors; will be <b>propagated to the caller</b>
     * (note: do not throw TransactionException subclasses here!)
     */
    protected void prepareForCommit(DefaultTransactionStatus status) {
    }

    /**
     * Perform an actual commit of the given transaction.
     * <p>An implementation does not need to check the "new transaction" flag
     * or the rollback-only flag; this will already have been handled before.
     * Usually, a straight commit will be performed on the transaction object
     * contained in the passed-in status.
     * @param status the status representation of the transaction
     * @throws TransactionException in case of commit or system errors
     * @see DefaultTransactionStatus#getTransaction
     */
    protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;

    /**
     * Perform an actual rollback of the given transaction.
     * <p>An implementation does not need to check the "new transaction" flag;
     * this will already have been handled before. Usually, a straight rollback
     * will be performed on the transaction object contained in the passed-in status.
     * @param status the status representation of the transaction
     * @throws TransactionException in case of system errors
     * @see DefaultTransactionStatus#getTransaction
     */
    protected abstract void doRollback(DefaultTransactionStatus status) throws TransactionException;

    /**
     * Set the given transaction rollback-only. Only called on rollback
     * if the current transaction participates in an existing one.
     * <p>The default implementation throws an IllegalTransactionStateException,
     * assuming that participating in existing transactions is generally not
     * supported. Subclasses are of course encouraged to provide such support.
     * @param status the status representation of the transaction
     * @throws TransactionException in case of system errors
     */
    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
        throw new IllegalTransactionStateException(
                "Participating in existing transactions is not supported - when 'isExistingTransaction' " +
                        "returns true, appropriate 'doSetRollbackOnly' behavior must be provided");
    }

    /**
     * Register the given list of transaction synchronizations with the existing transaction.
     * <p>Invoked when the control of the Spring transaction manager and thus all Spring
     * transaction synchronizations end, without the transaction being completed yet. This
     * is for example the case when participating in an existing JTA or EJB CMT transaction.
     * <p>The default implementation simply invokes the {@code afterCompletion} methods
     * immediately, passing in "STATUS_UNKNOWN". This is the best we can do if there's no
     * chance to determine the actual outcome of the outer transaction.
     * @param transaction transaction object returned by {@code doGetTransaction}
     * @param synchronizations List of TransactionSynchronization objects
     * @throws TransactionException in case of system errors
     * @see #invokeAfterCompletion(List, int)
     * @see TransactionSynchronization#afterCompletion(int)
     * @see TransactionSynchronization#STATUS_UNKNOWN
     */
    protected void registerAfterCompletionWithExistingTransaction(
            Object transaction, List<TransactionSynchronization> synchronizations) throws TransactionException {

        logger.debug("Cannot register Spring after-completion synchronization with existing transaction - " +
                "processing Spring after-completion callbacks immediately, with outcome status 'unknown'");
        invokeAfterCompletion(synchronizations, TransactionSynchronization.STATUS_UNKNOWN);
    }

    /**
     * Cleanup resources after transaction completion.
     * <p>Called after {@code doCommit} and {@code doRollback} execution,
     * on any outcome. The default implementation does nothing.
     * <p>Should not throw any exceptions but just issue warnings on errors.
     * @param transaction transaction object returned by {@code doGetTransaction}
     */
    protected void doCleanupAfterCompletion(Object transaction) {
    }


    //---------------------------------------------------------------------
    // Serialization support
    //---------------------------------------------------------------------

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Rely on default serialization; just initialize state after deserialization.
        ois.defaultReadObject();

        // Initialize transient fields.
        this.logger = LogFactory.getLog(getClass());
    }

    /**
     * Holder for suspended resources.
     * Used internally by {@code suspend} and {@code resume}.
     */
    protected static class SuspendedResourcesHolder {

        private final Object suspendedResources;
        private List<TransactionSynchronization> suspendedSynchronizations;
        private String name;
        private boolean readOnly;
        private Integer isolationLevel;
        private boolean wasActive;

        private SuspendedResourcesHolder(Object suspendedResources) {
            this.suspendedResources = suspendedResources;
        }

        private SuspendedResourcesHolder(
                Object suspendedResources, List<TransactionSynchronization> suspendedSynchronizations,
                String name, boolean readOnly, Integer isolationLevel, boolean wasActive) {
            this.suspendedResources = suspendedResources;
            this.suspendedSynchronizations = suspendedSynchronizations;
            this.name = name;
            this.readOnly = readOnly;
            this.isolationLevel = isolationLevel;
            this.wasActive = wasActive;
        }
    }

}
