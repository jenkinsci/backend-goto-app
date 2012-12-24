<!DOCTYPE html>
<html lang="en">
<head>
    <% adjunct 'org.kohsuke.stapler.backbone' %>
    <% adjunct 'org.kohsuke.stapler.bootstrap-responsive' %>
    <% adjunct 'org.kohsuke.stapler.require' %>
    <% adjunct 'org.kohsuke.stapler.fontawesome' %>
    <link rel="stylesheet" type="text/css" href="app.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="app.js" type="text/javascript"></script>
</head>

<body>
<div class="row-fluid">
    <div id="content" class="span10 offset1">
        <h1>Your Jenkins installations</h1>
        <div id="installations">
        </div>
        <div id="footer">
            <a  class="add btn btn-primary">
                <i class="icon-plus"></i> Add
            </a>
        </div>
    </div>
</div>
</body>
</html>
