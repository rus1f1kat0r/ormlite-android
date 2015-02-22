package com.j256.ormlite.android.apptools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.GenericRowMapper;

import java.sql.SQLException;

/**
 * Hacked version of a CursorAdapter.  The idea here is that you don't load a whole
 * result list from the db into memory.  However, from a main thread "no I/O" perspective,
 * there can be trouble if you have a huge result set.  Just keep that in mind.
 * <p/>
 * http://stackoverflow.com/a/11751153/227313
 * <p/>
 * Created by kgalligan on 2/21/15.
 */
public abstract class OrmLiteResultsAdapter<T, ID extends Number> extends BaseAdapter
{
	private final Context context;
	private final Dao<T, ID> dao;
	private final GenericRowMapper<T> rowMapper;
	private AndroidDatabaseResults databaseResults;

	public OrmLiteResultsAdapter(Context context, Dao<T, ID> dao) throws SQLException
	{
		this(context, dao, null);
	}

	public OrmLiteResultsAdapter(Context context, Dao<T, ID> dao, AndroidDatabaseResults databaseResults) throws SQLException
	{
		this.context = context.getApplicationContext();
		this.dao = dao;
		this.rowMapper = dao.getSelectStarRowMapper();
		this.databaseResults = databaseResults;
	}

	public T getItem(int position)
	{
		try
		{
			if (databaseResults != null && databaseResults.moveAbsolute(position))
			{
				return rowMapper.mapRow(databaseResults);
			} else
			{
				return null;
			}
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public int getCount()
	{
		return databaseResults == null ? 0 : databaseResults.getCount();
	}

	public long getItemId(int position)
	{
		try
		{
			if (databaseResults == null)
				return 0;

			//TODO: Find id without inflating item.
			T item = getItem(position);
			ID id = dao.extractId(item);

			return id.longValue();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public View getView(int position, View convertView, ViewGroup viewGroup)
	{
		T data = getItem(position);

		if (data == null)
		{
			throw new IllegalStateException("couldn't move cursor to position " + position);
		}
		View v;
		if (convertView == null)
		{
			v = newView(context, position, data, viewGroup);
		} else
		{
			v = convertView;
		}
		bindView(v, position, data);
		return v;
	}

	public abstract View newView(Context context, int position, T data, ViewGroup parent);

	public View newDropDownView(Context context, int position, T data, ViewGroup parent)
	{
		return newView(context, position, data, parent);
	}

	public abstract void bindView(View view, int position, T data);

	public void changeResults(AndroidDatabaseResults databaseResults)
	{
		this.databaseResults = databaseResults;
		notifyDataSetChanged();
	}
}
