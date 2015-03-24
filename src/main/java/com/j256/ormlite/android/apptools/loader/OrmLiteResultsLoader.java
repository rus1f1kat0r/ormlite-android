package com.j256.ormlite.android.apptools.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.DaoObserver;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;

import static com.j256.ormlite.stmt.StatementBuilder.StatementType.SELECT;

/**
 * Similar to CursorLoader, but aligns better with how OrmLite handles data.  Modded from emmby's model based on CursorAdapter.
 *
 * @author kgalligan, emmby
 */
public class OrmLiteResultsLoader<T> extends AsyncTaskLoader<AndroidDatabaseResults> implements DaoObserver {

	protected final Dao<T, ?> dao;
	protected final PreparedQuery<T> query;
	protected AndroidDatabaseResults databaseResults;

	public OrmLiteResultsLoader(Context context, Dao<T, ?> dao, PreparedQuery<T> query) {
		super(context);
		this.dao = dao;
		this.query = query;
	}

	@Override
	public AndroidDatabaseResults loadInBackground() {
		AndroidDatabaseResults cursor;
		try {
			dao.setObjectCache(true);
			DatabaseConnection connection = dao.getConnectionSource().getReadOnlyConnection();
			AndroidCompiledStatement statement = (AndroidCompiledStatement) query.compile(connection, SELECT);
			cursor = new AndroidDatabaseResults(statement.getCursor(), dao.getObjectCache());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// fill the cursor with results
		cursor.getCount();
		return cursor;
	}

	@Override
	public void deliverResult(AndroidDatabaseResults newCursor) {
		if (isReset()) {
			// an async query came in while the loader is stopped
			if (newCursor != null) {
				newCursor.close();
			}
			return;
		}

		AndroidDatabaseResults oldCursor = databaseResults;
		databaseResults = newCursor;

		if (isStarted()) {
			super.deliverResult(newCursor);
		}

		// close the old cursor if necessary
		if (oldCursor != null && oldCursor != newCursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	@Override
	protected void onStartLoading() {
		// start watching for dataset changes
		dao.registerObserver(this);

		if (databaseResults == null) {
			forceLoad();
		} else {
			deliverResult(databaseResults);
			if (takeContentChanged()) {
				forceLoad();
			}
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(AndroidDatabaseResults cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (databaseResults != null) {
			if (!databaseResults.isClosed()) {
				databaseResults.close();
			}
			databaseResults = null;
		}

		// stop watching for changes
		dao.unregisterObserver(this);
	}

	public void onChange() {
		onContentChanged();
	}
}
