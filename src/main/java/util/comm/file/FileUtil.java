package util.comm.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import util.comm.array.CollectionUtil;
import util.comm.file.bean.FileBean;
import util.comm.lambda.exception.Handler;
import util.comm.lambda.exception.SimpleExec;

public class FileUtil {

	public static String readText(String relative, boolean trim) throws IOException {
		StringBuilder text = new StringBuilder();
		try (BufferedReader reader = Files.newBufferedReader(
				Paths.get(FileUtil.removeProtocol(FileUtil.class.getClassLoader().getResource(relative))))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				text.append(trim ? line : line.trim());
			}
		}
		return text.toString();
	}
	
	public static String readText(InputStream content,String charset, boolean trim) {
		StringBuilder text = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(content,charset))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				text.append(trim ? line : line.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text.toString();
	}
	
	public static String readText(byte[] bytes) {
		try {
			return new String(bytes,"uf-8");
		} catch (UnsupportedEncodingException e) {
			return "Error:not support charset!!";
		}
	}

	public static void writeText(String relative, String file, String text) throws IOException {
		System.out.println(FileUtil.removeProtocol(FileUtil.class.getClassLoader().getResource(relative)));
		try (BufferedWriter writer = Files.newBufferedWriter(
				Paths.get(FileUtil.removeProtocol(FileUtil.class.getClassLoader().getResource(relative)) + "/" + file),
				StandardOpenOption.CREATE_NEW)) {
			writer.write(text);
		}

	}

	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (!file.exists() && file.isDirectory()) {
			return false;
		}

		return file.delete();
	}

	public static void notExistCreate(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static String onlyDir(String fileUrl) {
		int index = fileUrl.lastIndexOf("/");
		int indexR = fileUrl.lastIndexOf("\\");
		int end = 0;
		if (index > 0 && indexR < index) {
			end = index;
		} else if (indexR > 0 && indexR > index) {
			end = index;
		}
		return fileUrl.substring(0, end);
	}

	public static File newFile(String fp) {
		notExistCreate(onlyDir(fp));
		return new File(fp);
	}

	public static File[] getSameNameFiles(String fp) {
		int endIndexE = fp.lastIndexOf(".");
		int endIndexP = fp.lastIndexOf("/");
		String fName = fp.substring(endIndexP + 1, endIndexE < 0 ? fp.length() : endIndexE);
		String fatherPath = fp.substring(0, endIndexP + 1);
		File dir = new File(fatherPath);
		File[] files = dir.listFiles((File f, String str) -> {
			return str.substring(0, str.lastIndexOf(".")).equals(fName);
		});
		return files;
	}

	public static boolean checkFileNameExist(String fp) {
		return getSameNameFiles(fp).length != 0;
	}

	public static boolean checkFileNameExistAndDelete(String fp) {
		File[] files = getSameNameFiles(fp);
		if (files.length == 0) {
			return false;
		}
		Arrays.stream(files).forEach(File::delete);
		return true;
	}

	public static ArrayList<String> getAllFileNameIn(String dir) {
		ArrayList<String> list = new ArrayList<>();
		File file = new File(dir);
		if (!file.exists()) {
			return null;
		}

		if (file.isFile()) {
			list.add(file.getName());
			return list;
		}

		file.listFiles((File f) -> {
			String n = f.getName();
			int end = n.lastIndexOf(".");
			// System.out.println(f.getName());
			if (f.isFile())
				list.add(n.substring(0, end < 0 ? n.length() : end));
			return true;
		});
		return list;
	}

	public static String generateFile(String base, FileNameGenerator generator) {
		FileUtil.notExistCreate(base);
		ArrayList<String> names = getAllFileNameIn(base);
		String name = generator.generate();
		while (names.contains(name))
			name = generator.generate();

		return name;
	}

	public static String removeProtocol(URL url) {
		String protocol = url.getProtocol();
		return (String) SimpleExec.exec(data -> {
			return URLDecoder.decode(url.toString().replaceAll(protocol + ":/", ""), "utf-8");
		}, Handler.PRINTTRACE);

	}

	public static String getwebRoot(String proname) {
		String root = System.getProperty("user.dir");
		String end_str = root.substring(root.lastIndexOf(File.separator) + 1);
		if (end_str.equals("bin")) {
			root = root.substring(0, root.lastIndexOf(File.separator) + 1);
		}

		return root + "webapps/" + proname + "/";
	}

	public static void clearDir(String dir) {
		File file = new File(dir);

		if (!file.isDirectory()) {
			return;
		}

		if (!file.exists()) {
			file.mkdirs();
			return;
		}

		Arrays.stream(file.listFiles()).forEach(File::delete);
	}

	public static String ext(String fp) {
		int last_dot_index = fp.lastIndexOf(".");
		if (last_dot_index < 0) {
			return "";
		}

		return fp.substring(last_dot_index + 1);
	}

	public static ArrayList<FileBean> recursiveFileIn(String path) {
		ArrayList<FileBean> files = new ArrayList<>();
		LinkedList<String> root = new LinkedList<>();
		root.add(path);
		while (!root.isEmpty()) {
			root.addAll(0, filesIn(root.pop(), files));
		}
		return files;
	}

	public static ArrayList<String> filesIn(String path, ArrayList<FileBean> files) {
		File file = new File(path);
		ArrayList<String> sub_paths = new ArrayList<>();
		if (file.exists() && file.isDirectory()) {
			file.list((f, n) -> {
				File f_ = new File(path + File.separator + n);
				if (f_.isDirectory()) {
					sub_paths.add(f_.getAbsolutePath());
				} else {
					FileBean fBean = new FileBean();
					files.add(fBean.setName(n).setPath(f_.getPath()).setLast_modified("" + f.lastModified()));
				}
				return true;
			});
		}
		return sub_paths;
	}

	public static interface FileNameGenerator {
		public String generate();
	}

	public static InputStream getProjectInputStream(String path) {
		System.out.println(ClassLoader.getSystemResource(path));
		return ClassLoader.getSystemResourceAsStream(path);
	}

	public static byte[] getBytes(InputStream stream) {
		try {
			//httpclient的流不能关闭
			byte[] buffer = new byte[1024];
			ArrayList<Object> list=new ArrayList<>();
			int len;
			while ((len=stream.read(buffer)) > 0) {
				if (len<1024) {
					list.add(CollectionUtil.trimArray(buffer, len));
					break;
				}
				list.add(buffer);
				buffer = new byte[1024];
			}
			return (byte[])CollectionUtil.mergeArrays(list, byte.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static byte[] getBytes(String abPath) {
		try(InputStream input=Files.newInputStream(Paths.get(abPath))){
			return getBytes(input);
		} catch (IOException e) {
			return null;
		}
	}
	
	public static byte[] getBytes(File file) {
		return getBytes(file.getAbsolutePath());
	}

	public static void writeBytes(byte[] bytes, String path) {
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path))) {
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getProjectAb(String path) {
		return newFile(path).getAbsolutePath();
	}

	
	public static String getProjectNearAb(String path) {
		return removeProtocol(ClassLoader.getSystemResource(path));
	}

	public static void main(String[] args) {
		System.out.println(getProjectNearAb(""));
	}
}
