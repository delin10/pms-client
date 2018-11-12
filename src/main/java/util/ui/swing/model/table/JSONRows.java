package util.ui.swing.model.table;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import util.ui.swing.bean.KV;

public class JSONRows {
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private ArrayList<JSONObject> list;

	public static JSONRows create() {
		return new JSONRows();
	}

	public JSONRows init() {
		list = new ArrayList<>();
		return this;
	}

	public JSONRows parse(String json) {
		if (list.size() != 0) {
			list = new ArrayList<>();
		}
		return addAll(json);
	}

	public JSONRows addAll(String json) {
		Lock lock_ = lock.writeLock();
		lock_.lock();
		try {
			JSONArray array = JSONArray.parseArray(json);
			array.stream().map(Object::toString).map(JSONObject::parseObject).forEach(list::add);
		} finally {
			lock_.unlock();
		}
		return this;
	}

	public JSONRows add(String json) {
		Lock lock_ = lock.writeLock();
		lock_.lock();
		try {
			JSONObject jo = JSONObject.parseObject(json);
			list.add(jo);
			return this;
		} finally {
			lock_.unlock();
		}
	}

	public JSONRows set(int row, String json) {
		Lock lock_ = lock.writeLock();
		lock_.lock();
		try {
			JSONObject jo = JSONObject.parseObject(json);
			list.set(row, jo);
			return this;
		} finally {
			lock_.unlock();
		}
	}

	public JSONRows remove(int row) {
		Lock lock_ = lock.writeLock();
		lock_.lock();
		try {
			list.remove(row);
		} finally {
			lock_.unlock();
		}
		return this;
	}

	public String get(int row, String col) {
		Lock lock_ = lock.readLock();
		lock_.lock();
		try {
			return list.get(row).getString(col);
		} finally {
			lock_.unlock();
		}
	}

	public int size() {
		return list.size();
	}

	public static void main(String... args) {
		ArrayList<KV> list = new ArrayList<>();
		list.add(new KV().setTitle("12").setField("12"));
		System.out.println(JSONArray.parseArray(JSON.toJSONString(list)).get(0));
	}
}
