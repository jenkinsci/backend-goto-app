<!DOCTYPE html>
<html lang="en">
  <head>
    <% adjunct 'org.kohsuke.stapler.backbone' %>
    <% adjunct 'org.kohsuke.stapler.require' %>
  </head>

  <body>
    <script>var crumb = "<%= org.kohsuke.stapler.WebApp.getCurrent().getCrumbIssuer().issueCrumb() %>";</script>
    <div id="recipes"></div>
    <script src="app.js" type="text/javascript"></script>
  </body>
</html>
