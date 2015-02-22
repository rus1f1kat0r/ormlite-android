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
See Ormlite docs and example apps for more detail.

### Add, Update, and Query

Create some data with the Dao instance.  Run queries.  You can directly query data, or use some helper classes (Loader, Adapter, more to come...)

## Performance

ORM's have a dubious reputation with regards to performance.  In some cases this may be due to bad implementions, but its
  often due to the user not understanding what will produce good and bad outcomes.  As with any tool, you need to understand
  what its doing and how to use it.  Using an ORM doesn't mean you don't need to understand SQL and databases.  It just makes
  using them easier and more productive.  For a more detailed discussion on OrmLite/Android performance, [PERFORMANCE.md](see here).

### Overview

OrmLite, like all ORM frameworks, has strengths and weaknesses.  Make your architectural decisions accordingly.

#### Create/Update

Single-entity creates and updates perform about as fast as direct SQL calls.  Although reflection should add overhead, the
amount of overhead is dwarfed by the time spent saving and writing data and transactions.  OrmLite is great for data writes
because you get the benefits of it being object based, without performance issues.

**Always do bulk writes in a transaction**

Outside of a transaction, multiple data writes are incredibly slow, regardless of how you're doing them (SQL or ORM).

#### Simple Queries

Single-table queries have some performance overhead when compared with straight SQL or source-gen ORM tools.  In large
queries (10k rows) it ranges between 50%-100% overhead.  That means if it takes 3 seconds to load 10k rows in SQL, it'll
take 4.5-6 in OrmLite.  In many applications, you're not loading anywhere near that much data, and the overhead is meaningless.
However, that'll be a decision you'll need to make.

#### Join Queries

Joins or foreign collections are significantly worse.  If you're doing large queries, use SQL.  If you're doing small queries,
again, the total overhead is minimal, but it is definitely slower than straight SQL.

### Recommendations

An important rule of software development is **don't optimize too early**.  Coding with an ORM is generally simpler and less
error prone (not everybody will agree with that, but as a contributor to an ORM framework, I'm obviously biased).  I would
code and iterate on a db design with entity objects, then pick out parts of the app to query with straight SQL as needed.  In
general, only large join queries really need direct SQL queries.  Everything else performs well.  YMMV.

## Future Directions

The OrmLite/Android code has been iterated since 2009/2010, but hasn't had major changes.  We're discussing and working
on the next big version.  Things (maybe) on the table:

1) @DatabaseQuery - Join queries are pretty slow.  A query object, that can involve joins and specific fields, which
should allow great performance with more powerful queries available with straight SQL.

2) SqlCipher - Encrypted database support.

3) RxJava bindings - Similar to https://github.com/square/sqlbrite, but with an ORM vibe.

4) Source-gen - This is much more in the maybe pile.  It would be a pretty big departure from the current design, but we'll 
see if we can get this in there.
