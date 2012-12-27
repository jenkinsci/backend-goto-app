What?
-----

This application is essentially a redirection switch board to jump
to a specific URL within everyone's Jenkins instance.

It lets us create an URL like <http://goto.jenkins-ci.org/?go=manage>
and share those with others. When you follow this link, it navigates
the browser to the `/manage` URL within Jenkins.


How?
----
Since everyone's Jenkins installation lives in diffrent URLs and we have
no way of knowing them, this application asks the user to register
the locations of their Jenkins installations. And to do this, each user
gets authenticated with their `jenkins-ci.org` account (via OpenID.)

<http://goto.jenkins-ci.org/?go=manage> will then ask the user to decide
which instance he/she wants to go to. This confirmation step also help
prevents the mass XSS/CSRF exploitation by forcing the request to be GET
and require manual clicking once.


Why?
----
The aim of this application is to encourage seamless navigation from
the internet into people's Jenkins instances.

For example, with this mechanism, Jenkins  Wiki pages
can have a hyperlink that points to the configuration page of a specific plugin.
The troubleshooting instructions can point directly to various pages.


Future
------
With a bit more improvement to Jenkins core, this can make various
user experience even smoother.

For example, we can create an "install this plugin" button on Wiki,
which allows those who are browsing the page to install this plugin
with just a couple of mouse clicks.

Simiarly, it can allow users to import job configurations from elsewhere
(aka Jenkins recipe plugin.)