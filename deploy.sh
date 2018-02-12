heroku ps:scale web=0
heroku war:deploy target/team-caring.war --app ttth-team-map
heroku ps:scale web=1