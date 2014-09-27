package com.jiahaoliuliu.storyteller.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;

/**
 * Class utilized to save the user elemental data persistently.
 */
public class Preferences {

	// The id of the booleans
	// Update the method clearUserData when modified
	public enum BooleanId {

		// The default value of the boolean id
		DEFAULT_BOOLEAN_ID;
		
		public static BooleanId toBooleanId(String booleanId) {
			try {
				return valueOf(booleanId);
			} catch (Exception ex) {
				return DEFAULT_BOOLEAN_ID;
			}
		}
	}
	
	// The id of the strings
	// Update the method clearUserData when modified
	public enum StringId {
        // The unique id of parse users
        PARSE_USER_NAME,

		// The default value of the string id
		DEFAULT_STRING_ID;
		
		public static StringId toStringId(String stringId) {
			try {
				return valueOf(stringId);
			} catch (Exception ex) {
				return DEFAULT_STRING_ID;
			}
		}
	}

	public enum IntId {
		// The default id
		DEFAULT_INT_ID;
		public static IntId toIntId(String intId) {
			try {
				return valueOf(intId);
			} catch (Exception ex) {
				return DEFAULT_INT_ID;
			}
		}
	}

	public enum DoubleId {
		// The default id
		DEFAULT_DOUBLE_ID;
		
		public static DoubleId toDoubleId(String doubleId) {
			try {
				return valueOf(doubleId);
			} catch (Exception ex) {
				return DEFAULT_DOUBLE_ID;
			}
		}
	}
	
	// The id of the long
	// It is used mainly to store the date
	public enum LongId {
		// The default value of the boolean id
		DEFAULT_LONG_ID;
		
		public static LongId toLongId(String longId) {
			try {
				return valueOf(longId);
			} catch (Exception ex) {
				return DEFAULT_LONG_ID;
			}
		}
		
	}
	
	// The id of the list Strings
	// Update the method clearUserData when modified
	public enum ListStringId {
		// The default value of the list string id
		DEFAULT_LIST_STRING_ID;
		public static ListStringId toListStringId(String listStringId) {
			try {
				return valueOf(listStringId);
			} catch (Exception ex) {
				return DEFAULT_LIST_STRING_ID;
			}
		}
	}

	public enum HashMapIntegerId {
		DEFAULT_HASH_MAP_INTEGER_ID;
		public static HashMapIntegerId toHashMahIntegerId(String hashMapIntegerId) {
			try {
				return valueOf(hashMapIntegerId);
			} catch (Exception ex) {
				return DEFAULT_HASH_MAP_INTEGER_ID;
			}
		}
	}

	public enum HashMapListStringId {
		DEFAULT_HASH_MAP_LIST_STRING_ID;
		public static HashMapListStringId toHashMapListStringId(String hashMapListStringId) {
			try {
				return valueOf(hashMapListStringId);
			} catch (Exception ex) {
				return DEFAULT_HASH_MAP_LIST_STRING_ID;
			}
		}
	}

    /**
     * The tag utilized for the log.
     */
    private static final String LOG_TAG = Preferences.class.getSimpleName();

    /**
     * The name of the file utilized to store the data.
     */
    private static final String FILE_NAME = "StoryTeller.Preferences";

    //The default values
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final String DEFAULT_STRING = null;
    
    // It doesn't matter what value has been set, it will never be used
    private static final Integer DEFAULT_INT = -1;

    // It doesn't matter what value has been set, it will never be used
    // Because the double is saved as long.
    private static final Double DEFAULT_DOUBLE = -1.0;

    // It doesn't matter what value has been set, it will never be used.
    private static final Long DEFAULT_LONG = Long.valueOf(-1);

    // The default data for static set
    private static final HashSet<String> DEFAULT_HASH_SET = null;

    private static final List<String> DEFAULT_LIST_STRING = null;
    
    private static final String SEPARATOR = "_";

    /**
     * The context passed by any Android's component.
     */
    private Context mContext;

    /**
     * The shared preferences to save/restore the data.
     */
    private SharedPreferences mSharedPreferences;

    /**
     * The editor to save the data.
     */
    private SharedPreferences.Editor mEditor;

    public static class SingletonHolder {
        public static final Preferences INSTANCE = new Preferences();
    }

    private Preferences() {}

    /**
     * The main constructor.
     * @param context The context passed by any Android's component.
     */
    public void initialize(Context context) {
        this.mContext = context;

        // The user shared preferences
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public boolean hasBeenInitialized() {
        return ((mContext != null) && (mSharedPreferences != null) && (mEditor != null));
    }

    //=========================================== public methods ==============================
    // Boolean
    /**
     * Get the data from shared preference
     * @param booleanId The id of the data to get
     * @return          The data if it has been saved
     *                  false otherwise
     */
    public boolean getBoolean(BooleanId booleanId) {
    	boolean bool = mSharedPreferences.getBoolean(booleanId.toString(), DEFAULT_BOOLEAN);
    	return bool;
    	
    }
    
    public void setBoolean(BooleanId booleanId, boolean bool) {
    	// The data will be set if it is not the default one
    	if (booleanId != BooleanId.DEFAULT_BOOLEAN_ID) {
            mEditor.putBoolean(booleanId.toString(), bool);
            mEditor.commit();
    	}
    }

    public boolean contains(BooleanId booleanId) {
        return mSharedPreferences.contains(booleanId.toString());
    }

    // String
    public String getString(StringId stringId) {
    	String string = mSharedPreferences.getString(stringId.toString(), DEFAULT_STRING);
    	return string;
    	
    }
    
    public void setString(StringId stringId, String string) {
    	// The data will be set if the id is not the default one
    	if (stringId != StringId.DEFAULT_STRING_ID) {
            mEditor.putString(stringId.toString(), string);
            mEditor.commit();
    	}
    }

    public boolean contains(StringId stringId) {
        return mSharedPreferences.contains(stringId.toString());
    }

    // Integer
    /**
     * Get the data in the shared preferences. If it is not set, return null
     * @param intId The id of the data saved
     * @return      The data if it has been saved
     *              null otherwise
     */
    public Integer getInt(IntId intId) {
    	if (mSharedPreferences.contains(intId.toString())) {
    		return mSharedPreferences.getInt(intId.toString(), DEFAULT_INT);
    	} else {
    		return null;
    	}
    }

    public void setInt(IntId intId, Integer integer) {
    	// The data will be set if the id is not the default one
    	if (intId != IntId.DEFAULT_INT_ID) {
            mEditor.putInt(intId.toString(), integer);
            mEditor.commit();
    	}
    }
    
    public boolean contains(IntId intId) {
        return mSharedPreferences.contains(intId.toString());
    }

    // Double
    /**
     * Get the data in the shared preferences. If it is not set, return null
     * @param doubleId The id of the data saved
     * @return      The data if it has been saved
     *              null otherwise
     */
    public Double getDouble(DoubleId doubleId) {
    	if (mSharedPreferences.contains(doubleId.toString())) {
    		return
    			Double.longBitsToDouble(
                        mSharedPreferences.getLong(doubleId.toString(),
    					DEFAULT_LONG));
    	} else {
    		return null;
    	}
    }

    public void setDouble(DoubleId doubleId, double doubleData) {
    	// The data will be set if the id is not the default one
    	if (doubleId != DoubleId.DEFAULT_DOUBLE_ID) {
            mEditor.putLong(doubleId.toString(), Double.doubleToRawLongBits(doubleData));
            mEditor.commit();
    	}
    }
    
    public boolean contains(DoubleId doubleId) {
        return mSharedPreferences.contains(doubleId.toString());
    }

    // Long
    /**
     * Get the data saved in the shared preferences.
     * @param longId The id of the data saved
     * @return       The data if it has been set
     *               null otherwise
     */
    public Long getLong(LongId longId) {
    	if (mSharedPreferences.contains(longId.toString())) {
    		return mSharedPreferences.getLong(longId.toString(), DEFAULT_LONG);
    	} else {
    		return null;
    	}
    }
    
    public void setLong(LongId longId, Long longData) {
    	// The data will be set if it is not the default one
    	if (longId != LongId.DEFAULT_LONG_ID) {
            mEditor.putLong(longId.toString(), longData);
            mEditor.commit();
    	}
    }

    public boolean contains(LongId longId) {
        return mSharedPreferences.contains(longId.toString());
    }

    /**
     * Remove all the content of the shared preferences
     */
    public void clearAll() {
        mEditor.clear();
        mEditor.commit();
    }
}
