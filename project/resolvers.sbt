(sys.env.get("MAVEN_REPO_USERNAME"), sys.env.get("MAVEN_REPO_PASSWORD")) match {
  case (Some(username), Some(password)) =>
    println("Maven Repo Username and Password found, add Reposilite Repo")
    credentials += Credentials("Reposilite", "repo.hxdts.com", username, password)
  case _ =>
    credentials ++= Seq()
}

// USER HOME credentials
Some(Path.userHome / ".sbt" / "credentials")
  .filter(_.exists())
  .filter(_.canRead)
  .fold(credentials ++= Seq())(file => credentials += Credentials(file))

ThisBuild / resolvers ++= Seq("hxdc-maven-repository-releases" at "https://repo.hxdts.com/releases")
