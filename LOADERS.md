# Overview

Loaders are a construct in Android that allow you to load data in a background thread, and automatically update the view based on
underlying data modifications.

[http://developer.android.com/guide/components/loaders.html](http://developer.android.com/guide/components/loaders.html)

Loaders are generally pitched to be used with ContentProviders.  I (generally) suggest not using ContentProvider if you're
 not sharing data outside of your app, and the point of an ORM framework is to allow you to more easily access databases
 directly, so there's really not much reason to use them (IMHO).

Loaders can be used with other types of data.  However, the loader lifecycle is relatively complex.  The loader implementation
here, OrmLiteResultsLoader, loads an instance of AndroidDatabaseResults, which can be used with OrmLiteResultsAdapter.