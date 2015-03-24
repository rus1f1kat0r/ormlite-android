package com.j256.ormlite.android.apptools.loader;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Similar to CursorLoader, but aligns better with how OrmLite handles data.  Modded from emmby's model based on CursorAdapter.
 *
 * @author kgalligan, emmby
 */
public class OrmLiteListLoader<T> extends AbstractOrmLiteLoader<T, List<T>>
{
	public OrmLiteListLoader(Context context, Dao<T, ?> dao, PreparedQuery<T> query)
	{
		super(context, dao, query);
	}

	@Override
	protected void resetData(List<T> databaseResults)
	{
		//Do nothing
	}

	@Override
	public List<T> loadInBackground()
	{
		try
		{
			return dao.query(query);
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}
