package com.github.ezh.common.util;

public class RedisUtils {
    public static final String separator = ":";
    public static final String underline = "_";
    public static final String wildcard = "*";
    public static final String separator_wildcard = ":*";

    public static final String KINDERGARTEN = "kindergarten";
    public static final String DAILY_RECOMMENDATION = "daily_recommendation:";
    public static final String OFFICE_CLASS_MONITOR_LIST = "monitor_list:";
    public static final String COURSE_LIST_BOOKID = "course_list:";
    public static final String BOOKSHELF_BOOK_LIST = "bookshelf_book_list";
    public static final String RECIPES_DATE = "recipes_date:";
    public static final String ADDRESS_LIST_USERID = "address_list:";
    public static final String CLASS_LIST_USERID = "class_list:";
    public static final String USER_LOGIN_CLIENT_STATUS = "user_login_client_status:";
    public static final String SINGLE_WORK_DATE = "single_work_date:";
    public static final String SINGLE_MONTH_WORK_DATE = "single_month_work_date:";
    public static final String CLASS_WORK_DATE = "class_work_date:";

    public static final String SINGLE_TEMPERATURE_DATE = "single_temperature_date:";
    public static final String SINGLE_MONTH_TEMPERATURE_DATE = "single_month_temperature_date:";
    public static final String CLASS_TEMPERATURE_DATE = "class_temperature_date:";

    //通知
    public static final String NOTICELIST_OFFICE_CLASS_USER_LIMIT = "noticelist_office_class_user_limit:";
    public static final String NOTICEDETAIL_NOTICE = "noticedetail_notice:";
    public static final String NOTICEREADSTATUSLIST_OFFICE_NOTICE = "noticereadstatuslist_office_notice:";

    //亲子任务
    public static final String TASKLIST_OFFICE_CLASS_USER_LIMIT = "tasklist_office_class_user_limit:";
    public static final String TASKDETAIL_TSAK = "taskdetail_task:";
    public static final String TASKUSERDETAIL_TSAK = "taskuserdetail_task:";
    public static final String TASKREADSTATUSLIST_OFFICE_CLASS_USER_TASK = "taskreadstatuslist_office_class_user_task:";

    //请假
    public static final String LEAVELIST_OFFICE_CLASS_USER_LIMIT = "leavelist_office_class_user_limit:";
    public static final String LEAVEDETAIL_LEAVE = "leavedetail_leave:";

    //班级圈
    public static final String CLASSCIRCLELIST_OFFICE_CLASS_LIMIT = "classcirclelist_office_class_limit:";

    //特别关注
    public static final String SPECIALATTENTION_OFFICE_CLASS_USER = "specialattention_office_class_user:";



    /******************************            后台相关操作时需要删除的REDIS           **************************/
    //字典
    public static final String SYSTEM_DICT_USERFAMILY = "system_dict:userfamily";
    public static final String SYSTEM_DICT_APPBANNER = "system_dict:appbanner";
    public static final String SYSTEM_DICT_PERIOD = "system_dict:period";
    public static final String SYSTEM_DICT_DEFAULTPERIOD = "system_dict:defaultperiod";

    //栏目
    public static final String SYSTEM_ITEM_ALL_COLUMN1 = "system_item:all_column1";
    public static final String SYSTEM_ITEM_ALL_COLUMN2 = "system_item:all_column2";
    public static final String SYSTEM_ITEM_ALL_HOT_WORD = "system_item:all_hot_word";
    public static final String SYSTEM_ITEM_CODE = "system_item:code:";
    public static final String SYSTEM_ITEM_ID = "system_item:id:";

    //资源
    public static final String SYSTEM_RESOURCE_COLUMN1_COLUMN2_PERIOD_USER_LIMIT = "system_resource_column1_column2_period_user_limit:";

    //资源 or 栏目
    public static final String SYSTEM_ITEM_RESOURCE_SEARCH_PERIOD_ISKIND = "system_item_resource_search_period_iskind:";

    //班级管理： 用户
    public static final String SYSTEM_USER_CLASSMANAGE_OFFICE_CLASS_USER = "system_user_classmanage_office_class_user:";



    public static final long TWO_HOUR = 7200l;
    public static final long FOUR_HOUR = 14400l;
    public static final long SIX_HOUR = 21600l;
    public static final long EIGHT_HOUR = 28800l;

    public static final long SEVEN_DAYS = 3600 * 24 * 7l;
}

