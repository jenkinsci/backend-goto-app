<!DOCTYPE html>
<html lang="en">
  <head>
    <% adjunct 'org.kohsuke.stapler.backbone' %>
    <% adjunct 'org.kohsuke.stapler.require' %>
      <link rel="stylesheet" type="text/css" href="app.css"/>
  </head>

  <body>
    <script>var crumb = "<%= org.kohsuke.stapler.WebApp.getCurrent().getCrumbIssuer().issueCrumb() %>";</script>
    <ul id="recipes"></ul>
    <script src="app.js" type="text/javascript"></script>
  </body>
</html>
