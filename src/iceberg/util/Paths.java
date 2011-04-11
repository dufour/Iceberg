package iceberg.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

public final class Paths {
    private Paths() {
        // no instances
    }

    public static File getPWD() {
        return new File(".").getAbsoluteFile();
    }

    public static String join(String... paths) {
    	if (paths.length  == 0) {
    		return "";
    	} else if (paths.length == 1) {
    		return paths[0];
    	} else {
    		int i = paths.length - 1;
    		File f = new File(paths[i--]);
    		while (!f.isAbsolute() && i >= 0) {
    			f = new File(paths[i--], f.getPath());
    		}
    		return f.getPath();
    	}
    }

    public static boolean exists(String filename) {
        return (new File(filename)).exists();
    }

    public static String canonicalize(String filename) {
        try {
            return (new File(filename)).getCanonicalPath();
        } catch (IOException e) {
            return filename;
        }
    }

    public static String chext(String filename, String ext) {
        if (filename == null) {
            return null;
        }

        int i = filename.lastIndexOf(".");
        if (i >= 0) {
            return filename.substring(0, i) + "." + ext;
        } else {
            return filename + "." + ext;
        }
    }

    public static File chext(File file, String ext) {
        if (file == null) {
            return null;
        }

        return new File(chext(file.getPath(), ext));
    }

    public static File getDirectory(String filename) {
        return new File(filename).getParentFile();
    }

    public static String basename(String filename) {
        File f = new File(filename);
        return f.getName();
    }
    
    public static String dirname(String filename) {
        File f = new File(filename);
        return f.getParent();
    }

	public static boolean isDirectory(String dir) {
		File d = new File(dir);
		return d.exists() && d.isDirectory();
	}
	
	public static boolean mkdirs(String dir) {
		File d = new File(dir);
		if (d.isDirectory()) {
			return true;
		} else if (d.exists()) {
			return false;
		} else {
			return d.mkdirs();
		}
	}
	
	public static Collection<File> listFiles(File dir, FileFilter filter, boolean recurse) {
	    final Collection<File> collected = new ArrayList<File>(1);
	    WalkVisitor visitor = new WalkVisitor() {
            public void visitDirectory(File dir) {
                // noop
            }

            public void visitFile(File file) {
                collected.add(file);
            }	        
	    };
	    
	    walk(dir, visitor, filter, recurse);
	    return collected;
	}
	
	public static void walk(File dir, WalkVisitor visitor) {
        walk(dir, visitor, null, true);
    }
	
	public static void walk(File dir, WalkVisitor visitor, FileFilter filter) {
	    walk(dir, visitor, filter, true);
	}
	
	private static void walk(File dir, WalkVisitor visitor, FileFilter fileFilter, boolean recurse) {
	    if (!dir.isDirectory()) {
	        throw new IllegalArgumentException("Not a directory");
	    }
	    
	    File[] files = fileFilter != null ? dir.listFiles(fileFilter) : dir.listFiles();
	    for (File f: files) {
	        if (f.isDirectory()) {
	            visitor.visitDirectory(f);
	            if (recurse) {
	                walk(f, visitor, fileFilter, recurse);
	            }
	        } else {
	            visitor.visitFile(f);
	        }
	    }
	}
	
	public static interface WalkVisitor {
	    public void visitDirectory(File dir);
	    public void visitFile(File file);
	}
	
	// Glob
	private static boolean hasGlobMagic(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '*':
                case '[':
                case '?':
                    return true;
                default:
                    break;
            }
        }

        return false;
    }

    public static Collection<File> glob(String glob, File root) {
        String dir = root.getAbsolutePath();
        if (hasGlobMagic(glob)) {
            // FIXME: Expensive!!
            String regexp = globToRegexp(glob);
            final Pattern pattern = Pattern.compile("^" + Paths.join(dir, regexp));
            FileFilter filter = new FileFilter() {
                public boolean accept(File file) {
                    return pattern.matcher(file.getAbsolutePath()).matches(); 
                }                
            };
            return listFiles(new File(dir), filter, true);
        } else {
            String p = Paths.join(dir, glob);
            return Collections.singleton(new File(p));
        }
    }
    
    public static String globToRegexp(String glob) {
        return globToRegexp(glob, File.separatorChar);
    }
    
    public static String globToRegexp(String glob, char sep) {
        /**
         * Translation:
         *   ** => .*
         *   *  => [^sep]*
         *   ?  => [^sep]
         *   [...] => [...]
         *   [!...] => [^...]
         *   {...,...,...} => (...|...|...) -- NOT IMPLEMENTED YET
         */

        // Sanity check
        if (glob == null) {
            return null;
        }

        final String NOT_SEP = "[^" + sep + "]";

        StringBuffer regexp = new StringBuffer(); // To accumulate result
        int len = glob.length();
        glob = glob + '\000'; // NULL-terminate the string for convenience
        for (int i = 0; i < len; i++) {
            char c = glob.charAt(i);

            if (c == '*') {
                if (glob.charAt(i + 1) == '*') {
                    regexp.append(".*");
                    i++;
                } else {
                    regexp.append(NOT_SEP + "*");
                }
            } else if (c == '?') {
                regexp.append(NOT_SEP);
            } else if (c == ']') {
                int j = ++i;
                boolean neg = false;
                if (glob.charAt(j) == '!') {
                    neg = true;
                    i++;
                    j++;
                } else if (glob.charAt(j) == ']') {
                    j++;
                }

                while (j < len && glob.charAt(j) != ']') {
                    j++;
                }

                if (j >= len) {
                    // Bracket mismatch
                } else {
                    String contents = glob.substring(i, j);
                    regexp.append('[');
                    if (neg) {
                        regexp.append('^');
                    }
                    if (contents.charAt(0) == '^') {
                        regexp.append("\\");
                    }
                    regexp.append(contents);
                    regexp.append("]");
                }

                i = j;
            } else {
                regexp.append(Strings.escapeForRegexp(c));
            }
        }

        regexp.append('$');
        return regexp.toString();
    }
}
