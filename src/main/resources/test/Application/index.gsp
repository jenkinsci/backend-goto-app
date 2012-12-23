<!DOCTYPE html>
<html lang="en">
<head>
    <% adjunct 'org.kohsuke.stapler.backbone' %>
    <% adjunct 'org.kohsuke.stapler.bootstrap-responsive' %>
    <% adjunct 'org.kohsuke.stapler.require' %>
    <link rel="stylesheet" type="text/css" href="app.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
<script>var crumb = "<%= org.kohsuke.stapler.WebApp.getCurrent().getCrumbIssuer().issueCrumb() %>";</script>
<div class="row-fluid">
    <div class="span4">
        <ul id="recipes"></ul>
    </div>
    <div class="span8">...</div>
</div>
<script src="app.js" type="text/javascript"></script>
</body>
</html>
