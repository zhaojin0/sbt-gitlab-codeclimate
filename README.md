# SBT checkstyle to Gitlab CodeClimate

This plugin can convert checkstyle file to codeclimate.json,
also supported multi-modules project aggregate. 

## Usage

enable plugin on project or root project

```sbt
// enable plugin
enablePlugins(GitlabCodeclimatePlugin)
```

## Configuration keys

`checkstyleFile` location to checkstyle file location in this project. 

## Tasks

run task in sbt shell

### project

Convert

```sbt
gitlabCodeclimate
```

### multi-module project

Aggregate

```sbt
gitlabCodeclimateAggregate
```
