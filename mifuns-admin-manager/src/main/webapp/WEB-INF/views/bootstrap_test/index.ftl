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