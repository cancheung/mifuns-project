package com.mifuns.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.*;

/**
 * @author Stony
 * Created Date : 2016/4/19  10:42
 */
public class JedisTemplate implements InitializingBean,Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5469261780433279435L;

	private static final Logger logger = LoggerFactory.getLogger(JedisSentinelTemplate.class);

    private boolean initialized = false;
    private boolean enableDefaultSerializer = true;
    private JedisPool firstPool;
    private RedisSerializer<Object> defaultSerializer = new JdkSerializationRedisSerializer();
    private RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    private RedisSerializer<String> keySerializer = null;
    private RedisSerializer<Object> valueSerializer = null;


    public static final long DEFAULT_RETURN_COUNT = 0L;

    //服务器节点
    private Map<Integer, JedisPool> shards = new HashMap<Integer, JedisPool>();
    private int shardsCount = 0;

    /**
     * default key serializer is {@link #keySerializer}
     * defualt value serializer is {@link #defaultSerializer}
     */
    public void afterPropertiesSet() {
        if(initialized) return;
        if (this.shards == null || this.shards.isEmpty()) {
            throw new IllegalArgumentException("Property 'firstPool'/'shards' is required");
        }
        this.shardsCount = shards.size();
        logger.info("shards size {}", this.shardsCount);
        boolean defaultUsed = false;
        if (enableDefaultSerializer) {
            if (keySerializer == null) {
                keySerializer = stringSerializer;
                defaultUsed = true;
            }
            if (valueSerializer == null) {
                valueSerializer = defaultSerializer;
                defaultUsed = true;
            }
        }
        if (enableDefaultSerializer && defaultUsed) {
            Assert.notNull(defaultSerializer, "default serializer null and not all serializers initialized");
        }
        initialized = true;
    }

    public static String format(String f, Object... args){
        return String.format(f,args);
    }

    /** ####################################################    字符串(String)      ########################################################### **/
    /**
     * 将字符串值value关联到key。
     * 如果key已经持有其他值，SET就覆写旧值，无视类型。
     * @param key
     * @param value
     * @param seconds
     * @return 总是返回OK，因为SET不可能失败。
     */
    public String setStr(String key,String value,Integer seconds){
        Jedis jedis = getJedis(key);
        String result = null;
        if(jedis != null) {
            try {
                if(seconds != null && seconds > 0){
                    result = jedis.setex(key, seconds, value);
                }else {
                    result = jedis.set(key, value);
                }
            } catch (Exception e) {
                logger.error(format("set key[%s],val[%s]出错:", key,value), e);
            } finally {
                close(jedis);
            }
        }

        return result;
    }
    /**
     * 将字符串值value关联到key。
     * @param key
     * @param value
     * @return 总是返回OK，因为SET不可能失败。
     */
    public String set(String key,String value,Integer seconds){
        return setStr(key, value, seconds);
    }
    /**
     * 将字符串值value关联到key。
     * 如果key已经持有其他值，SET就覆写旧值，无视类型。
     * @param key
     * @param value
     * @return 总是返回OK，因为SET不可能失败。
     */
    public String set(String key,String value){
        return setStr(key, value, null);
    }

    /**
     * 将字符串值value关联到key。
     * 如果key已经持有其他值，SET就覆写旧值，无视类型。
     * @param key
     * @param value
     * @param seconds
     * @return 总是返回OK，因为SET不可能失败。
     */
    public String set(String key,Object value,Integer seconds){
        Jedis jedis = getJedis(key);
        String result = null;
        if(jedis != null) {
            try {
                if(seconds != null && seconds > 0){
                    result = jedis.setex(keySerializer.serialize(key), seconds, valueSerializer.serialize(value));
                }else {
                    result = jedis.set(keySerializer.serialize(key), valueSerializer.serialize(value));
                }
            } catch (Exception e) {
                logger.error(format("set key[%s],val[%s]出错:", key, value), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 将字符串值value关联到key。
     * 如果key已经持有其他值，SET就覆写旧值，无视类型
     * @param key
     * @param value
     * @return 总是返回OK，因为SET不可能失败。
     */
    public String set(String key, Object value){
        return set(key, value,null);
    }
    /**
     * key的值。
     * 如果key不存在，返回nil。
     * @param key
     * @return
     */
    public String getStr(String key){
        String result = null;
        Jedis jedis = getJedis(key);
        if(jedis != null){
            try {
                result = jedis.get(key);
            } catch (Exception e) {
                logger.error(format("get key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * key的值。
     * 如果key不存在，返回nil。
     * @param key
     * @return
     */
    public Object get(String key){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = valueSerializer.deserialize(jedis.get(keySerializer.serialize(key)));
            } catch (Exception e) {
                logger.error(format("get key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 将key的值设为value，当且仅当key不存在。
     * 若给定的key已经存在，则SETNX不做任何动作。
     * SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。
     * @param key
     * @param value
     * @return 设置成功，返回1。设置失败，返回0。
     */
    public Long setnx(String key, Serializable value){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.setnx(keySerializer.serialize(key), valueSerializer.serialize(value));
            } catch (Exception e) {
                logger.error(format("setnx key[%s],val[%s]出错:", key, value), e);
            } finally {
                close(jedis);
            }
        }
        return DEFAULT_RETURN_COUNT;
    }
    /**
     * 将key的值设为value，当且仅当key不存在。
     * 若给定的key已经存在，则SETNX不做任何动作。
     * SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。
     * @param key String
     * @param value String
     * @return 设置成功，返回1。设置失败，返回0。
     */
    public Long setnx(String key, String value){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.setnx(key, value);
            } catch (Exception e) {
                logger.error(format("setnx key[%s],val[%s]出错:", key, value), e);
            } finally {
                close(jedis);
            }
        }

        return DEFAULT_RETURN_COUNT;
    }

    /**
     * 返回key所储存的字符串值的长度。
     * 当key储存的不是字符串值时，返回一个错误
     * @param key String
     * @return 字符串值的长度。当 key不存在时，返回0。
     */
    public Long strlen(String key){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.strlen(key);
            } catch (Exception e) {
                logger.error(format("strlen key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return DEFAULT_RETURN_COUNT;
    }
    /**
     * 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
     * 如果key不存在，APPEND就简单地将给定key设为value，就像执行SET key value一样。
     * @param key String
     * @param value String
     * @return 追加value之后，key中字符串的长度
     */
    public Long append(String key,String value){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.append(key,value);
            } catch (Exception e) {
                logger.error(format("append key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return DEFAULT_RETURN_COUNT;
    }
    /**
     * 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
     * 如果key不存在，APPEND就简单地将给定key设为value，就像执行SET key value一样。
     * @param key String
     * @param value String
     * @return 追加value之后，key中字符串的长度
     */
    public Long append(String key, Serializable value){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.append(keySerializer.serialize(key), valueSerializer.serialize(value));
            } catch (Exception e) {
                logger.error(format("append key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return DEFAULT_RETURN_COUNT;
    }

    /**
     * 将key中储存的数字值增一。
     * 如果key不存在，以0为key的初始值，然后执行INCR操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在64位(bit)有符号数字表示之内。
     * @param key
     * @return 执行INCR命令之后key的值。
     */
    public Long incr(String key) {
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        try {
            return jedis.incr(keySerializer.serialize(key));
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }
    /**
     * 将key所储存的值加上增量increment。
     * 如果key不存在，以0为key的初始值，然后执行INCR操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在64位(bit)有符号数字表示之内。
     * @param key
     * @param increment
     * @return 执行INCR命令之后key的值。
     */
    public Long incrby(String key, long increment) {
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        try {
            return jedis.incrBy(keySerializer.serialize(key),increment);
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }

    /**
     * 将key中储存的数字值减一。
     * 如果key不存在，以0为key的初始值，然后执行INCR操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在64位(bit)有符号数字表示之内。
     * @param key
     * @return 执行DECR命令之后key的值。
     */
    public Long decr(String key) {
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        try {
            return jedis.decr(keySerializer.serialize(key));
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }
    /**
     * 将key所储存的值减去减量decrement。
     * 如果key不存在，以0为key的初始值，然后执行INCR操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在64位(bit)有符号数字表示之内。
     * @param key
     * @param increment
     * @return 执行DECR命令之后key的值。
     */
    public Long decrby(String key, long increment) {
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        try {
            return jedis.decrBy(keySerializer.serialize(key),increment);
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            close(jedis);
        }
        return result;
    }

    /** ####################################################    哈希表(Hash)      ########################################################### **/
    /**
     * 将哈希表key中的域field的值设为value。
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param values Map<String, Object>
     * @param seconds
     * @return
     */
    public Long hset(String key, Map<String, Object> values, Integer seconds){
        Jedis jedis = getJedis(key);
        Long re = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                if(values == null || values.isEmpty()) return re;
                for(Iterator<String> it = values.keySet().iterator();it.hasNext();){
                    String mkey = it.next();
                    re += jedis.hset(keySerializer.serialize(key), keySerializer.serialize(mkey), valueSerializer.serialize(values.get(mkey)));
                }
            } catch (Exception e) {
                logger.error(format("hset key[%s], val[%s]出错:", key, values), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }

        return re;
    }

    /**
     * 将哈希表key中的域field的值设为value。
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param values
     * @return
     */
    public Long hset(String key, Map<String,Object> values){
        return hset(key, values, null);
    }

    /**
     * 将哈希表key中的域field的值设为value。
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public Long hset(String key,String field,String value, Integer seconds){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.hset(key, field, value);
            } catch (Exception e) {
                logger.error(format("hset key[%s], field[%s], val[%s]出错:", key,field, value), e);
            } finally {
                if (seconds != null && seconds > 0) {
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return DEFAULT_RETURN_COUNT;
    }

    /**
     * 将哈希表key中的域field的值设为value。
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key,String field,String value){
        return hset(key, field, value, null);
    }

    /**
     * 将哈希表key中的域field的值设为value。
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value Object
     * @param seconds
     * @return
     */
    public Long hset(String key, String field, Object value, Integer seconds) {
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.hset(keySerializer.serialize(key), keySerializer.serialize(field), valueSerializer.serialize(value));
            } catch (Exception e) {
                logger.error(format("hset key[%s], field[%s], val[%s]出错:", key, field, value), e);
            } finally {
                if (seconds != null && seconds > 0) {
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return DEFAULT_RETURN_COUNT;
    }
    /**
     * 将哈希表key中的域field的值设为value。
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value Object
     * @return
     */
    public Long hset(String key,String field, Object value){
        return hset(key, field, value, null);
    }
    /**
     * 返回哈希表key中，所有的域和值。
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * @param key String
     * @return 对返回值进行反序列化Map<String,Object>
     */
    public Map<String,Object> hgetAll(String key){
        Jedis jedis = getJedis(key);
        if(jedis != null){
            try {
                Map<String,Object> map = new HashMap<>();
                Map<byte[],byte[]> re = jedis.hgetAll(keySerializer.serialize(key));
                for(Map.Entry<byte[],byte[]> entry : re.entrySet()){
                    map.put(keySerializer.deserialize(entry.getKey()), valueSerializer.deserialize(entry.getValue()));
                }
                return map;
            } catch (Exception e) {
                logger.error(format("hgetAll key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 返回哈希表key中，所有的域和值。
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * @param key
     * @return Map<String,String>
     */
    public Map<String,String> hgetAllStr(String key){
        Jedis jedis = getJedis(key);
        if(jedis != null){
            try {
                return jedis.hgetAll(key);
            } catch (Exception e) {
                logger.error(format("hgetAll key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 返回哈希表key中，一个或多个给定域的值。
     * 如果给定的域不存在于哈希表，那么返回一个nil值。
     * 因为不存在的key被当作一个空哈希表来处理，所以对一个不存在的key进行HMGET操作将返回一个只带有nil值的表。
     * @param key
     * @param field
     * @return
     */
    public Object hget(String key,String field){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = valueSerializer.deserialize(jedis.hget(keySerializer.serialize(key), keySerializer.serialize(field)));
            } catch (Exception e) {
                logger.error(format("hget key[%s], field[%s]出错:", key,field), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    public Object hgetStr(String key,String field){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = jedis.hget(key,field);
            } catch (Exception e) {
                logger.error(format("hget key[%s], field[%s]出错:", key,field), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /** ####################################################    表(List)      ########################################################### **/

    /**
     * 将一个或多个值value插入到列表key的表头。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：比如对一个空列表(mylist)执行LPUSH mylist a b c，则结果列表为c b a，等同于执行执行命令LPUSH mylist a、LPUSH mylist b、LPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param seconds
     * @param values
     * @return
     */
    public Long lpush(String key,Integer seconds, String...values){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = jedis.lpush(key, values);
            } catch (Exception e) {
                logger.error(format("lpush key[%s], val[%s]出错:", key, values), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：比如对一个空列表(mylist)执行LPUSH mylist a b c，则结果列表为c b a，等同于执行执行命令LPUSH mylist a、LPUSH mylist b、LPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return
     */
    public Long lpush(String key,String...values){
        return lpush(key, null, values);
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：比如对一个空列表(mylist)执行LPUSH mylist a b c，则结果列表为c b a，等同于执行执行命令LPUSH mylist a、LPUSH mylist b、LPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param seconds
     * @param values
     * @return
     */
    public Long lpush(String key, Integer seconds, Object...values){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null){
            try {
                for(Object object : values) {
                    result += jedis.lpush(keySerializer.serialize(key), valueSerializer.serialize(object));
                }
            } catch (Exception e) {
                logger.error(format("lpush key[%s], val[%s]出错:", key, values), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：比如对一个空列表(mylist)执行LPUSH mylist a b c，则结果列表为c b a，等同于执行执行命令LPUSH mylist a、LPUSH mylist b、LPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return
     */
    public Long lpush(String key,Object...values){
        return lpush(key, null, values);
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：比如对一个空列表(mylist)执行LPUSH mylist a b c，则结果列表为c b a，等同于执行执行命令LPUSH mylist a、LPUSH mylist b、LPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param list
     * @param seconds
     * @return
     */
    public Long lpush(String key, Collection<?> list, Integer seconds){
        Jedis jedis = getJedis(key);
        Long re = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                for(Object value : list){
                    re += jedis.lpush(keySerializer.serialize(key), valueSerializer.serialize(value));
                }
            } catch (Exception e) {
                logger.error(format("lpush key[%s], val[%s]出错:", key, list), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return re;
    }

    /**
     * 将一个或多个值value插入到列表key的表头。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表头：比如对一个空列表(mylist)执行LPUSH mylist a b c，则结果列表为c b a，等同于执行执行命令LPUSH mylist a、LPUSH mylist b、LPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param list
     * @return
     */
    public Long lpush(String key, Collection<?> list){
        return lpush(key, list, null);
    }
    /**
     * 根据参数count的值，移除列表中与参数value相等的元素。
     * count的值可以是以下几种：
     * count > 0: 从表头开始向表尾搜索，移除与value相等的元素，数量为count。
     * count < 0: 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值。
     * count = 0: 移除表中所有与value相等的值。
     * @param key
     * @param count
     * @param value String
     * @return
     */
    public Long lrem(String key, int count, Serializable value) {
        Jedis jedis = getJedis(key);
        Long r = DEFAULT_RETURN_COUNT;
        try {
            return jedis.lrem(keySerializer.serialize(key), count, valueSerializer.serialize(value));
        } catch (Exception e) {
            logger.error(format("lrem key[%s], val[%s]出错:", key, value), e);
        } finally {
            close(jedis);
        }
        return r;
    }

    /**
     * 根据参数count的值，移除列表中与参数value相等的元素。
     * count的值可以是以下几种：
     * count > 0: 从表头开始向表尾搜索，移除与value相等的元素，数量为count。
     * count < 0: 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值。
     * count = 0: 移除表中所有与value相等的值。
     * @param key
     * @param count
     * @param value String
     * @return
     */
    public Long lrem(String key, int count, String value) {
        Jedis jedis = getJedis(key);
        Long r = DEFAULT_RETURN_COUNT;
        try {
            return jedis.lrem(key, count, value);
        } catch (Exception e) {
            logger.error(format("lrem key[%s], val[%s]出错:", key, value), e);
        } finally {
            close(jedis);
        }
        return r;
    }
    /**
     * 根据参数count的值，移除列表中与参数value相等的元素。
     * count的值可以是以下几种：
     * count > 0: 从表头开始向表尾搜索，移除与value相等的元素，数量为count。
     * count < 0: 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值。
     * count = 0: 移除表中所有与value相等的值。
     * @param key
     * @param count
     * @param value Object
     * @return
     */
    public Long lrem(String key, int count, Object value) {
        Jedis jedis = getJedis(key);
        Long r = DEFAULT_RETURN_COUNT;
        try {
            return jedis.lrem(keySerializer.serialize(key), count, valueSerializer.serialize(value));
        } catch (Exception e) {
            logger.error(format("lrem key[%s], val[%s]出错:", key, value), e);
        } finally {
            close(jedis);
        }
        return r;
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 举个例子，执行命令LTRIM list 0 2，表示只保留列表list的前三个元素，其余元素全部删除。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 当key不是列表类型时，返回一个错误。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Object ltrim(String key,long start,long end){
        Jedis jedis = getJedis(key);
        String result = null;
        try {
            result = jedis.ltrim(key,start,end);
        } catch (Exception e) {
            logger.error(format("ltrim key[%s], start[%s], end[%s]出错:", key, start,end), e);
        } finally {
            close(jedis);
        }
        return result;
    }
    /**
     * 将一个或多个值value插入到列表key的表尾。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表尾：比如对一个空列表(mylist)执行RPUSH mylist a b c，则结果列表为a b c，等同于执行命令RPUSH mylist a、RPUSH mylist b、RPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行RPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values String
     * @return 执行RPUSH操作后，表的长度。
     */
    public Long rpush(String key, String... values){
        return rpushStr(key, values);
    }
    /**
     * 将一个或多个值value插入到列表key的表尾。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表尾：比如对一个空列表(mylist)执行RPUSH mylist a b c，则结果列表为a b c，等同于执行命令RPUSH mylist a、RPUSH mylist b、RPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行RPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values String
     * @return 执行RPUSH操作后，表的长度。
     */
    public Long rpushStr(String key, String... values){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null){
            try {
                result = jedis.rpush(key, values);
            } catch (Exception e) {
                logger.error(format("rpush key[%s], val[%s]出错:", key, values), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /**
     * 将值value插入到列表key的表尾，当且仅当key存在并且是一个列表。
     * 和RPUSH命令相反，当key不存在时，RPUSHX命令什么也不做。
     * @param key
     * @param values String
     * @return RPUSHX命令执行之后，表的长度。
     */
    public Long rpushx(String key, String... values){
        return rpushxStr(key,values);
    }
    /**
     * 将值value插入到列表key的表尾，当且仅当key存在并且是一个列表。
     * 和RPUSH命令相反，当key不存在时，RPUSHX命令什么也不做。
     * @param key
     * @param values String
     * @return RPUSHX命令执行之后，表的长度。
     */
    public Long rpushxStr(String key, String... values){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null){
            try {
                result = jedis.rpushx(key, values);
            } catch (Exception e) {
                logger.error(format("rpushx key[%s], val[%s]出错:", key, values), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /**
     * 将一个或多个值value插入到列表key的表尾。
     * 如果有多个value值，那么各个value值按从左到右的顺序依次插入到表尾：比如对一个空列表(mylist)执行RPUSH mylist a b c，则结果列表为a b c，等同于执行命令RPUSH mylist a、RPUSH mylist b、RPUSH mylist c。
     * 如果key不存在，一个空列表会被创建并执行RPUSH操作。
     * 当key存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values Object
     * @return 执行RPUSH操作后，表的长度。
     */
    public Long rpush(String key, Object... values){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null){
            try {
                for(Object value : values) {
                    result += jedis.rpush(keySerializer.serialize(key), valueSerializer.serialize(value));
                }
            } catch (Exception e) {
                logger.error(format("rpush key[%s], val[%s]出错:", key, values), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 将值value插入到列表key的表尾，当且仅当key存在并且是一个列表。
     * 和RPUSH命令相反，当key不存在时，RPUSHX命令什么也不做。
     * @param key
     * @param values Object
     * @return RPUSHX命令执行之后，表的长度。
     */
    public Long rpushx(String key, Object... values){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null){
            try {
                for(Object value : values) {
                    result += jedis.rpushx(keySerializer.serialize(key), valueSerializer.serialize(value));
                }
            } catch (Exception e) {
                logger.error(format("rpushx key[%s], val[%s]出错:", key, values), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 移除并返回列表key的头元素
     * @param key
     * @return
     */
    public Object lpop(String key){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = valueSerializer.deserialize(jedis.lpop(keySerializer.serialize(key)));
            } catch (Exception e) {
                logger.error(format("lpop key[%s]出错:", key ), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 移除并返回列表key的头元素
     * @param key
     * @return
     */
    public String lpopStr(String key){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.lpop(key);
            } catch (Exception e) {
                logger.error(format("lpop key[%s]出错:", key ), e);
            } finally {
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 返回列表key中，下标为index的元素。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 如果key不是列表类型，返回一个错误。
     * @param key
     * @param index 下标
     * @return 列表中下标为index的元素。如果index参数的值不在列表的区间范围内(out of range)，返回nil。
     */
    public Object lindex(String key,int index){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return valueSerializer.deserialize(jedis.lindex(keySerializer.serialize(key),index));
            } catch (Exception e) {
                logger.error(format("lindex key[%s]出错:", key ), e);
            } finally {
                close(jedis);
            }
        }
        return null;
    }
    /**
     * 返回列表key中，下标为index的元素。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 如果key不是列表类型，返回一个错误。
     * @param key Key
     * @param index 下标
     * @return 列表中下标为index的元素。如果index参数的值不在列表的区间范围内(out of range)，返回nil。
     */
    public String lindexStr(String key,int index){
        Jedis jedis = getJedis(key);
        if(jedis != null) {
            try {
                return jedis.lindex(key,index);
            } catch (Exception e) {
                logger.error(format("lindex key[%s]出错:", key ), e);
            } finally {
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 移除并返回列表key的尾元素。
     * @param key
     * @return 列表的尾元素。当key不存在时，返回nil。
     */
    public Object rpop(String key){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = valueSerializer.deserialize(jedis.rpop(keySerializer.serialize(key)));
            } catch (Exception e) {
                logger.error(format("rpop key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }

        return result;
    }
    /**
     * 移除并返回列表key的尾元素。
     * @param key
     * @return 列表的尾元素。当key不存在时，返回nil。
     */
    public Object rpopStr(String key){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = jedis.rpop(key);
            } catch (Exception e) {
                logger.error(format("rpop key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }

        return result;
    }

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行LRANGE list 0 10，结果是一个包含11个元素的列表，这表明stop下标也在LRANGE命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的Range.new、Array#slice和Python的range()函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果start下标比列表的最大下标end(LLEN list减去1)还要大，或者start > stop，LRANGE返回一个空列表。
     * 如果stop下标比end下标还要大，Redis将stop的值设置为end。
     * @param key
     * @return
     */
    public List<?> lrange(String key){
        return lrange(key, 0, -1);
    }

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行LRANGE list 0 10，结果是一个包含11个元素的列表，这表明stop下标也在LRANGE命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的Range.new、Array#slice和Python的range()函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果start下标比列表的最大下标end(LLEN list减去1)还要大，或者start > stop，LRANGE返回一个空列表。
     * 如果stop下标比end下标还要大，Redis将stop的值设置为end。
     * @param key
     * @param start
     * @return
     */
    public List<?> lrange(String key,long start){
        return lrange(key, start, -1);
    }

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行LRANGE list 0 10，结果是一个包含11个元素的列表，这表明stop下标也在LRANGE命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的Range.new、Array#slice和Python的range()函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果start下标比列表的最大下标end(LLEN list减去1)还要大，或者start > stop，LRANGE返回一个空列表。
     * 如果stop下标比end下标还要大，Redis将stop的值设置为end。
     *  返回值 也参与序列化
     * @param key 参与序列化
     * @param start
     * @param end
     * @return
     */
    public List<?> lrange(String key,long start,long end){
        Jedis jedis = getJedis(key);
        List<Object> re = new ArrayList<>();
        if(jedis != null) {
            try {
                List<byte[]> jres = jedis.lrange(keySerializer.serialize(key), start, end);
                for(byte[] jre : jres){
                    re.add(valueSerializer.deserialize(jre));
                }
            } catch (Exception e) {
                logger.error(format("lrange key[%s], start[%d], end[%d]出错:", key, start, end), e);
            } finally {
                close(jedis);
            }
        }

        return re;
    }

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 注意LRANGE命令和编程语言区间函数的区别
     * 假如你有一个包含一百个元素的列表，对该列表执行LRANGE list 0 10，结果是一个包含11个元素的列表，这表明stop下标也在LRANGE命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的Range.new、Array#slice和Python的range()函数。
     * 超出范围的下标
     * 超出范围的下标值不会引起错误。
     * 如果start下标比列表的最大下标end(LLEN list减去1)还要大，或者start > stop，LRANGE返回一个空列表。
     * 如果stop下标比end下标还要大，Redis将stop的值设置为end。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<?> lrangeStr(String key,long start,long end){
        Jedis jedis = getJedis(key);
        List<String> result = null;
        if(jedis != null) {
            try {
                result = jedis.lrange((key), start, end);
            } catch (Exception e) {
                logger.error(format("lrange key[%s], start[%s], end[%s]出错:", key, start, end), e);
            } finally {
                close(jedis);
            }
        }

        return result;
    }
    public List<?> lrangeStr(String key){
        return lrangeStr(key, 0, -1);
    }



    /**
     * 返回列表key的长度。
     * 如果key不存在，则key被解释为一个空列表，返回0.
     * 如果key不是列表类型，返回一个错误。
     * @param key
     * @return
     */
    public Long llen(String key) {
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        try {
            result = jedis.llen(key.getBytes());
        } catch (Exception e) {
            logger.error(format("llen key[%s]出错:", key), e);
        } finally {
            close(jedis);
        }
        return result;
    }
    /** ####################################################    集合(Set)      ########################################################### **/
    /**
     * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
     * 假如key不存在，则创建一个只包含member元素作成员的集合。
     * 当key不是集合类型时，返回一个错误。
     * @param key Key
     * @param seconds 过期时间
     * @param members String
     * @return
     */
    public Long sadd(String key, Integer seconds, String...members){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = jedis.sadd(key, members);
            } catch (Exception e) {
                logger.error(format("sadd key[%s], val[%s]出错:", key, members), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }

        return result;
    }
    /**
     * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
     * 假如key不存在，则创建一个只包含member元素作成员的集合。
     * 当key不是集合类型时，返回一个错误。
     * @param key Key
     * @param member String
     * @return
     */
    public Long sadd(String key, String member){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = jedis.sadd(key, member);
            } catch (Exception e) {
                logger.error(format("sadd key[%s], val[%s]出错:", key, member), e);
            } finally {
                close(jedis);
            }
        }

        return result;
    }
    /**
     * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
     * 假如key不存在，则创建一个只包含member元素作成员的集合。
     * 当key不是集合类型时，返回一个错误。
     * @param key Key
     * @param members String
     * @return
     */
    public Long sadd(String key,String...members){
        return sadd(key, null, members);
    }

    public Long sadd(String key, Serializable...members){
        return sadd(key, null, members);
    }
    /**
     * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
     * 假如key不存在，则创建一个只包含member元素作成员的集合。
     * 当key不是集合类型时，返回一个错误。
     * @param key Key
     * @param seconds 过期时间
     * @param members Serializable
     * @return
     */
    public Long sadd(String key,Integer seconds, Serializable...members){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                for(Serializable member: members) {
                    result += jedis.sadd(keySerializer.serialize(key), valueSerializer.serialize(member));
                }
            } catch (Exception e) {
                logger.error(format("sadd key[%s], val[%s]出错:", key, members), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
     * 假如key不存在，则创建一个只包含member元素作成员的集合。
     * 当key不是集合类型时，返回一个错误。
     * @param key
     * @param list Serializable
     * @param seconds 过期时间
     * @return
     */
    public Long sadd(String key, Collection<Serializable> list, Integer seconds){
        Jedis jedis = getJedis(key);
        Long re = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                for(Object value : list){
                    re += jedis.sadd(keySerializer.serialize(key), valueSerializer.serialize(value));
                }
            } catch (Exception e) {
                logger.error(format("sadd key[%s], val[%s]出错:", key, list), e);
            } finally {
                if(seconds != null && seconds > 0){
                    expire(key, seconds, jedis);
                }
                close(jedis);
            }
        }
        return re;
    }
    public Long sadd(String key, Collection<Serializable> list){
        return sadd(key, list, null);
    }

    /**
     * 移除并返回集合中的一个随机元素。
     * @param key
     * @return 被移除的随机元素。当key不存在或key是空集时，返回nil。
     */
    public Object spop(String key){
        Jedis jedis = getJedis(key);
        Object result = null;
        if(jedis != null) {
            try {
                result = valueSerializer.deserialize(jedis.spop(keySerializer.serialize(key)));
            } catch (Exception e) {
                logger.error(format("spop key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /**
     * 移除并返回集合中的一个随机元素。
     * @param key
     * @return 被移除的随机元素。当key不存在或key是空集时，返回nil。
     */
    public String spopStr(String key){
        Jedis jedis = getJedis(key);
        String result = null;
        if(jedis != null) {
            try {
                result = jedis.spop(key);
            } catch (Exception e) {
                logger.error(format("spop key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /**
     * 返回集合key中的所有成员。
     * @param key
     * @return 返回集合key中的所有成员。
     */
    public Set<?> smembersObject(String key){
        Jedis jedis = getJedis(key);
        Set<Object> result = new HashSet<>();
        if(jedis != null) {
            try {
                Set<byte[]> _rs = jedis.smembers(keySerializer.serialize(key));
                for(byte[] _by : _rs){
                    result.add(valueSerializer.deserialize(_by));
                }
            } catch (Exception e) {
                logger.error(format("smembers key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /**
     * 返回集合key中的所有成员。
     * @param key
     * @return 返回集合key中的所有成员。
     */
    public Set<String> smembers(String key){
        Jedis jedis = getJedis(key);
        Set<String> result = null;
        if(jedis != null) {
            try {
                result = jedis.smembers(key);
            } catch (Exception e) {
                logger.error(format("smembers key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    /** ####################################################    expire/exists/del      ########################################################### **/
    /**
     * 为给定key设置生存时间。
     * 当key过期时，它会被自动删除。
     * 在Redis中，带有生存时间的key被称作“易失的”(volatile)。
     * 在低于2.1.3版本的Redis中，已存在的生存时间不可覆盖。
     * 从2.1.3版本开始，key的生存时间可以被更新，也可以被PERSIST命令移除。(详情参见 http://redis.io/topics/expire)。
     * @param key
     * @return 设置成功返回1。 当key不存在或者不能为key设置生存时间时(比如在低于2.1.3中你尝试更新key的生存时间)，返回0。
     */
    public long expire(String key){
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = expire(key, 0, jedis);
            } catch (Exception e) {
                logger.error(format("expire key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /**
     * 为给定key设置生存时间。
     * 当key过期时，它会被自动删除。
     * 在Redis中，带有生存时间的key被称作“易失的”(volatile)。
     * 在低于2.1.3版本的Redis中，已存在的生存时间不可覆盖。
     * 从2.1.3版本开始，key的生存时间可以被更新，也可以被PERSIST命令移除。(详情参见 http://redis.io/topics/expire)。
     * @param key
     * @param seconds
     * @return 设置成功返回1。 当key不存在或者不能为key设置生存时间时(比如在低于2.1.3中你尝试更新key的生存时间)，返回0。
     */
    public long expire(String key,int seconds) {
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = expire(key, seconds, jedis);
            } catch (Exception e) {
                logger.error(format("expire key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /**
     * 为给定key设置生存时间。
     * 当key过期时，它会被自动删除。
     * 在Redis中，带有生存时间的key被称作“易失的”(volatile)。
     * 在低于2.1.3版本的Redis中，已存在的生存时间不可覆盖。
     * 从2.1.3版本开始，key的生存时间可以被更新，也可以被PERSIST命令移除。(详情参见 http://redis.io/topics/expire)。
     * @param key
     * @param jedis
     * @param seconds
     * @return 设置成功返回1。 当key不存在或者不能为key设置生存时间时(比如在低于2.1.3中你尝试更新key的生存时间)，返回0。
     */
    public long expire(String key,int seconds, Jedis jedis) {
        long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = jedis.expire(key,seconds);
            } catch (Exception e) {
                logger.error(format("expire key[%s],seconds[%s]出错:", key, seconds), e);
            }
        }
        return result;
    }

    /**
     * @param key
     * @return 若key存在，返回1，否则返回0。
     */
    public boolean exists(String key) {
        Jedis jedis = getJedis(key);
        boolean result = false;
        if(jedis != null) {
            try {
                result = jedis.exists(key);
            } catch (Exception e) {
                logger.error(format("exists key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }

    public Long del(String...keys){
        long result = DEFAULT_RETURN_COUNT;
        for(String key : keys){
            result += del(key);
        }
        return result;
    }
    public Long del(Set<String> keys){
        long result = DEFAULT_RETURN_COUNT;
        for(String key : keys){
            result += del(key);
        }
        return result;
    }
    public Long del(String key){
        if(key == null) return DEFAULT_RETURN_COUNT;
        Jedis jedis = getJedis(key);
        Long result = DEFAULT_RETURN_COUNT;
        if(jedis != null) {
            try {
                result = jedis.del(key);
            } catch (Exception e) {
                logger.error(format("删除key[%s]出错:", key), e);
            } finally {
                close(jedis);
            }
        }
        return result;
    }
    /** 过期时间 seconds 秒 **/
    public static final int DEFAULT_EXPIRE = 120;

    /**
     * 默认12秒超时
     * @param lockKey
     * @return
     */
    public boolean lock(String lockKey){
        return lock(lockKey,12);
    }
    /**
     *
     * @param lockKey 锁Key
     * @param timeout 超时 seconds 秒
     */
    public boolean lock(String lockKey, int timeout){
        return lock(lockKey,timeout,DEFAULT_EXPIRE);
    }

    /**
     *
     * @param lockKey Key
     * @param timeout 超时 seconds 秒
     * @param expireTime 过期时间 秒
     * @return
     */
    public boolean lock(String lockKey, int timeout, int expireTime){
        if(lockKey == null) return false;
        Jedis jedis = getJedis(lockKey);
        boolean locked = false;
        if(jedis != null) {
            try {
                try {
                    long _timeout = timeout * 1000000000L; //秒转换为纳秒
                    long nano = System.nanoTime();
                    final Random r = new Random();
                    while ((System.nanoTime() - nano) < _timeout) {
                        //设置成功，返回1。
                        if (jedis.setnx(lockKey, "1") == 1L) {
                            expire(lockKey, expireTime, jedis);
                            locked = true;
                            logger.debug("lock key = {},val = {},expire = {}.", lockKey, "1", expireTime);
                            break;
                        }
                        Thread.sleep(3, r.nextInt(500));
                    }
                } catch (Exception e) {}
            } catch (Exception e) {
                logger.error(format("lock key[%s],timeout[%s] 出错:", lockKey,timeout), e);
            } finally {
                close(jedis);
            }
        }
        return locked;
    }
    public Long unlock(String lockKey){
        if(lockKey == null) return DEFAULT_RETURN_COUNT;
        logger.debug("unlock key = {}.", lockKey);
        return del(lockKey);
    }

    /**
     *
     * @return 当前秒 seconds
     */
    public long currentTimeSeconds(){
        return (System.currentTimeMillis()/1000L);
    }

    public Jedis getJedis(String key){
        return getJedisPool(key).getResource();
    }
    final int hash(Object k) {
        int h = 0;
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    final int hash3(Object k) {
        int h = 0;
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        h = (h ^ (h >>> 7) ^ (h >>> 4));
        return h & (h >>> 1);
    }

    /**
     *
     * @param h
     * @param length 长度必须是2的幂次方
     * @return
     */
    static int indexFor(int h, int length) {
        return h & (length-1);
    }
    static int indexFor3(int h, int length) {
        return h % length;
    }
    public void close(Jedis jedis){
        if(jedis != null) jedis.close();
    }

    /**
     *
     * @param key
     * @return
     */
    public JedisPool getJedisPool(String key) {
        if(shardsCount == 1){
            return getFirstPool();
        }
        JedisPool pool;
        try {
            int h = hash(key);
            //key 值从 1 开始
            int index = (1 + indexFor(h, shardsCount));
            logger.debug("choice pool[{}] ,key[{}], hash[{}]", index, key, h);
            pool = this.shards.get(index);
        } catch (Exception e) {
            pool = getFirstPool();
        }
        return pool;
    }

    /**
     * 如果 firstPool 不为空，返回
     * @return firstPool
     */
    private JedisPool getFirstPool(){
        if(firstPool != null){
            return this.firstPool;
        }
        for(Map.Entry entry : this.shards.entrySet()){
            this.firstPool = this.shards.get(entry.getKey());
            return this.firstPool;
        }
        return  null;
    }
    public boolean isInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    public boolean isEnableDefaultSerializer() {
        return enableDefaultSerializer;
    }
    public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
        this.enableDefaultSerializer = enableDefaultSerializer;
    }

    /**
     * index 从 1 开始
     * @param firstPool
     */
    public void setFirstPool(JedisPool firstPool) {
        shards.put(1, firstPool);
        this.firstPool = shards.get(1);
    }
    public RedisSerializer<Object> getDefaultSerializer() {
        return defaultSerializer;
    }
    public void setDefaultSerializer(RedisSerializer<Object> defaultSerializer) {
        this.defaultSerializer = defaultSerializer;
    }
    public RedisSerializer<String> getKeySerializer() {
        return keySerializer;
    }
    public void setKeySerializer(RedisSerializer<String> keySerializer) {
        this.keySerializer = keySerializer;
    }
    public RedisSerializer<Object> getValueSerializer() {
        return valueSerializer;
    }
    public void setValueSerializer(RedisSerializer<Object> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public RedisSerializer<String> getStringSerializer() {
        return stringSerializer;
    }
    public void setStringSerializer(RedisSerializer<String> stringSerializer) {
        this.stringSerializer = stringSerializer;
    }

    public Map<Integer, JedisPool> getShards() {
        return shards;
    }

    public void setShards(Map<Integer, JedisPool> shards) {
        this.shards = shards;
    }

}