<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->

<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->

<!-- BEGIN HEAD -->
<head>
<#include '../common/common_head.ftl'>

</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="page-header-fixed">

	<!-- BEGIN HEADER -->
	<#include '../common/header_navbar.ftl'>
	<!-- END HEADER -->

    <div class="copyrights">Collect from <a href="http://www.cssmoban.com/" >网页模板</a></div>

	<!-- BEGIN CONTAINER -->

	<div class="page-container">

		<!-- BEGIN SIDEBAR -->
		<#include '../common/sidebar.ftl'>
		<!-- END SIDEBAR -->

		<!-- BEGIN PAGE -->

		<div class="page-content">

			<!-- BEGIN SAMPLE PORTLET CONFIGURATION MODAL FORM-->

			<div id="portlet-config" class="modal hide">

				<div class="modal-header">

					<button data-dismiss="modal" class="close" type="button"></button>

					<h3>Widget Settings</h3>

				</div>

				<div class="modal-body">

					Widget settings form goes here

				</div>

			</div>

			<!-- END SAMPLE PORTLET CONFIGURATION MODAL FORM-->

			<!-- BEGIN PAGE CONTAINER-->

			<div class="container-fluid">

				<!-- BEGIN PAGE HEADER-->
				<div class="row-fluid">
					<div class="span12">
						<!-- BEGIN PAGE TITLE & BREADCRUMB-->
						<h3 class="page-title">
							控制台
						</h3>
						<ul class="breadcrumb">
							<li>
								<i class="icon-home"></i>
								<a href="index.html">主页</a>
								<i class="icon-angle-right"></i>
							</li>
							<li class="pull-right no-text-shadow">
								<div id="dashboard-report-range" class="dashboard-date-range tooltips no-tooltip-on-touch-device responsive" data-tablet="" data-desktop="tooltips" data-placement="top" data-original-title="Change dashboard date range">
									<i class="icon-calendar"></i>
									<span></span>
									<i class="icon-angle-down"></i>
								</div>
							</li>
						</ul>
						<!-- END PAGE TITLE & BREADCRUMB-->
					</div>
				</div>
				<!-- END PAGE HEADER-->

                <!-- BEGIN PAGE CONTENT-->
                <div class="row-fluid">
                    <div class="span12">
                        <!-- BEGIN EXAMPLE TABLE PORTLET-->

                        <div class="portlet box light-grey">

                            <div class="portlet-title">

                                <div class="caption"><i class="icon-globe"></i>Managed Table</div>

                                <div class="tools">

                                    <a href="javascript:;" class="collapse"></a>

                                    <a href="#portlet-config" data-toggle="modal" class="config"></a>

                                    <a href="javascript:;" class="reload"></a>

                                    <a href="javascript:;" class="remove"></a>

                                </div>

                            </div>

                            <div class="portlet-body">

                                <div class="clearfix">

                                    <div class="btn-group">

                                        <button id="sample_editable_1_new" class="btn green">

                                            Add New <i class="icon-plus"></i>

                                        </button>

                                    </div>

                                    <div class="btn-group pull-right">

                                        <button class="btn dropdown-toggle" data-toggle="dropdown">Tools <i class="icon-angle-down"></i>

                                        </button>

                                        <ul class="dropdown-menu pull-right">

                                            <li><a href="#">Print</a></li>

                                            <li><a href="#">Save as PDF</a></li>

                                            <li><a href="#">Export to Excel</a></li>

                                        </ul>

                                    </div>

                                </div>

                                <table class="table table-striped table-bordered table-hover" id="sample_1">

                                    <thead>

                                    <tr>

                                        <th style="width:8px;"><input type="checkbox" class="group-checkable" data-set="#sample_1 .checkboxes" /></th>

                                        <th>Username</th>

                                        <th class="hidden-480">Email</th>

                                        <th class="hidden-480">Points</th>

                                        <th class="hidden-480">Joined</th>

                                        <th ></th>

                                    </tr>

                                    </thead>

                                    <tbody>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>shuxer</td>

                                        <td class="hidden-480"><a href="mailto:shuxer@gmail.com">shuxer@gmail.com</a></td>

                                        <td class="hidden-480">120</td>

                                        <td class="center hidden-480">12 Jan 2012</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>looper</td>

                                        <td class="hidden-480"><a href="mailto:looper90@gmail.com">looper90@gmail.com</a></td>

                                        <td class="hidden-480">120</td>

                                        <td class="center hidden-480">12.12.2011</td>

                                        <td ><span class="label label-warning">Suspended</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>userwow</td>

                                        <td class="hidden-480"><a href="mailto:userwow@yahoo.com">userwow@yahoo.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>user1wow</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">userwow@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td ><span class="label label-inverse">Blocked</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>restest</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">test@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>foopl</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>weep</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>coop</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>pppol</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>test</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>userwow</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">userwow@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td ><span class="label label-inverse">Blocked</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>test</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">test@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>goop</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>weep</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">15.11.2011</td>

                                        <td ><span class="label label-inverse">Blocked</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>toopl</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">16.11.2010</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>userwow</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">userwow@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">9.12.2012</td>

                                        <td ><span class="label label-inverse">Blocked</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>tes21t</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">test@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">14.12.2012</td>

                                        <td ><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>fop</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">13.11.2010</td>

                                        <td ><span class="label label-warning">Suspended</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>kop</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">17.11.2010</td>

                                        <td><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>vopl</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.11.2010</td>

                                        <td><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>userwow</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">userwow@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td><span class="label label-inverse">Blocked</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>wap</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">test@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">12.12.2012</td>

                                        <td><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>test</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">19.12.2010</td>

                                        <td><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>toop</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">17.12.2010</td>

                                        <td><span class="label label-success">Approved</span></td>

                                    </tr>

                                    <tr class="odd gradeX">

                                        <td><input type="checkbox" class="checkboxes" value="1" /></td>

                                        <td>weep</td>

                                        <td class="hidden-480"><a href="mailto:userwow@gmail.com">good@gmail.com</a></td>

                                        <td class="hidden-480">20</td>

                                        <td class="center hidden-480">15.11.2011</td>

                                        <td><span class="label label-success">Approved</span></td>

                                    </tr>

                                    </tbody>

                                </table>

                            </div>

                        </div>

                        <!-- END EXAMPLE TABLE PORTLET-->
                    </div>
				</div>
                <!-- END PAGE CONTENT-->


			</div>

			<!-- END PAGE CONTAINER-->    

		</div>

		<!-- END PAGE -->

	</div>

	<!-- END CONTAINER -->

	<!-- BEGIN FOOTER -->

	<#include '../common/footer.ftl'>

	<!-- END FOOTER -->

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

	<#include '../common/bootstrap_js.ftl'>

	<script>
		jQuery(document).ready(function() {
		   App.init(); // initlayout and core plugins
           //TableEditable.init();
			TableManaged.init();
		   //Index.init();
		   //Index.initJQVMAP(); // init index page's custom scripts
		   //Index.initCalendar(); // init index page's custom scripts
		   //Index.initCharts(); // init index page's custom scripts
		   //Index.initChat();
		   //Index.initMiniCharts();
		   //Index.initDashboardDaterange();
		   //Index.initIntro();
		});
	</script>

	<!-- END JAVASCRIPTS -->

</body>

<!-- END BODY -->

</html>