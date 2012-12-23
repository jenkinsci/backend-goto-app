<!DOCTYPE html>
<html lang="en">
  <head>
    <% adjunct 'org.kohsuke.stapler.backbone' %>
  </head>

  <body>
    <script>var crumb = "<%= org.kohsuke.stapler.WebApp.getCurrent().getCrumbIssuer().issueCrumb() %>";</script>
    <div id="recipes"></div>
    <script id="recipeTemplate" src="recipe.html" type="text/template">
    </script>
    <script src="app.js" type="text/javascript"></script>
  </body>
</html>
