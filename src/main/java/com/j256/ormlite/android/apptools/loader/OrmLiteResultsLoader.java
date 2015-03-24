package com.j256.ormlite.android.apptools.loader;

import android.content.Context;
import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;

import static com.j256.ormlite.stmt.StatementBuilder.StatementType.SELECT;

/**
 * Similar to CursorLoader, but aligns better with how OrmLite handles data.  Modded from emmby's model based on CursorAdapter.
 *
 * @author kgalligan, emmby
 */
public class OrmLiteResultsLoader<T> extends AbstractOrmLiteLoader<T, AndroidDatabaseResults>
{
	public OrmLiteResultsLoader(Context context, Dao<T, ?> dao, PreparedQuery<T> query)
	{
		super(context, dao, query);
	}

	@Override
	protected void resetData(AndroidDatabaseResults databaseResults)
	{
		if (databaseResults != null) {
			if (!databaseResults.isClosed()) {
				databaseResults.close();
			}
		}
	}

	@Override
	public AndroidDatabaseResults loadInBackground()
	{
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
}
