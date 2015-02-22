# Android Sqlite ORM

OrmLite extension for Android.  OrmLite provides a lightweight ORM framework best suited for simpler
implementations and relatively constrained environments.

## More Background Info

[http://ormlite.com/](http://ormlite.com/)
[http://en.wikipedia.org/wiki/Object-relational_mapping](ORM on Wiki)

## Android Specific Features

Ormlite for Android provides Android-specific Sqlite bindings, as well as support classes
to enable simple database development on Android.

## Getting Started

### Getting Libraries

If you're not using gradle, follow the ormlite.com link above and download both core and
android specific jars, and put them in your path.

For gradle builds, simply include the android-specific dependency (same thing for maven, etc).

```
compile 'com.j256.ormlite:ormlite-android:4.49'
```

### Initialize Your Database

Ormlite provides an extension of SQLiteOpenHelper, called OrmLiteSqliteOpenHelper, to manage your
database lifecycle.  It provides equivalent extension points, with some Ormlite specific additions.  You don't *need*
to use OrmLiteSqliteOpenHelper, but it will make implementation easier.

### Get a Reference

Originally Ormlite had helper Activity and Session classes to manage your db connections.  These are deprecated.  Do not
use them.  Please use your singleton creation method of choice.  The summary is you want to have one SQLiteOpenHelper in your process.  That
can be a singleton, dependency injection, etc.  More [http://kpgalligan.tumblr.com/post/109546839958/single-database-connection] (info here).

### Create an Entity Class

Your tables are represented by entity classes.  The class is annotated with @DatabaseTable, fields with @DatabaseField.
See Ormlite docs for more detail.

This package provides the Android specific functionality.  You will also need to download
the ormlite-core package as well.  Users that are connecting to SQL databases via JDBC
connections should download the ormlite-jdbc package instead of this Android one.

For more information, see the online documentation on the home page:

   http://ormlite.com/

Sources can be found online via Github:

   https://github.com/j256/ormlite-android

Enjoy,
Gray Watson
