package com.moocall.moocall.domain;

import android.app.Activity;
import com.moocall.moocall.C0530R;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class PostFilter {
    private Activity activity;
    private Boolean allCategory;
    private Boolean allUsers;
    private Boolean buySellCategory;
    private HashMap<Integer, String> categories;
    private Boolean cattleBreedingCategory;
    private String country;
    private Boolean dairyFarmingCategory;
    private Boolean farmingBusinessCategory;
    private Boolean farmingNewsCategory;
    private Boolean peopleILiked;

    public PostFilter(PostFilter postFilter) {
        setAllUsers(postFilter.getAllUsers());
        setPeopleILiked(postFilter.getPeopleILiked());
        setCountry(postFilter.getCountry());
        setAllCategory(postFilter.getAllCategory());
        setFarmingNewsCategory(postFilter.getFarmingNewsCategory());
        setDairyFarmingCategory(postFilter.getDairyFarmingCategory());
        setCattleBreedingCategory(postFilter.getCattleBreedingCategory());
        setBuySellCategory(postFilter.getBuySellCategory());
        setFarmingBusinessCategory(postFilter.getFarmingBusinessCategory());
    }

    public PostFilter(Activity activity) {
        setAllUsers(Boolean.valueOf(true));
        setPeopleILiked(Boolean.valueOf(false));
        setCountry(null);
        setAllCategory(Boolean.valueOf(true));
        setFarmingNewsCategory(Boolean.valueOf(false));
        setDairyFarmingCategory(Boolean.valueOf(false));
        setCattleBreedingCategory(Boolean.valueOf(false));
        setBuySellCategory(Boolean.valueOf(false));
        setFarmingBusinessCategory(Boolean.valueOf(false));
        this.activity = activity;
        setCategories();
    }

    public Boolean getAllUsers() {
        return this.allUsers;
    }

    public void setAllUsers(Boolean allUsers) {
        this.allUsers = allUsers;
    }

    public void toggleAllUsers() {
        if (getAllUsers().booleanValue()) {
            setAllUsers(Boolean.valueOf(false));
            setPeopleILiked(Boolean.valueOf(true));
            return;
        }
        setAllUsers(Boolean.valueOf(true));
        setPeopleILiked(Boolean.valueOf(false));
    }

    public Boolean getPeopleILiked() {
        return this.peopleILiked;
    }

    public void setPeopleILiked(Boolean peopleILiked) {
        this.peopleILiked = peopleILiked;
    }

    public void togglePeopleILiked() {
        if (getPeopleILiked().booleanValue()) {
            setPeopleILiked(Boolean.valueOf(false));
            setAllUsers(Boolean.valueOf(true));
            return;
        }
        setPeopleILiked(Boolean.valueOf(true));
        setAllUsers(Boolean.valueOf(false));
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getAllCategory() {
        return this.allCategory;
    }

    public void setAllCategory(Boolean allCategory) {
        this.allCategory = allCategory;
    }

    public void toggleAllCategory() {
        if (getAllCategory().booleanValue()) {
            setAllCategory(Boolean.valueOf(false));
            if (!getFarmingNewsCategory().booleanValue() && !getDairyFarmingCategory().booleanValue() && !getCattleBreedingCategory().booleanValue() && !getBuySellCategory().booleanValue() && !getFarmingBusinessCategory().booleanValue()) {
                setAllCategory(Boolean.valueOf(true));
                return;
            }
            return;
        }
        setAllCategory(Boolean.valueOf(true));
        setFarmingNewsCategory(Boolean.valueOf(false));
        setDairyFarmingCategory(Boolean.valueOf(false));
        setCattleBreedingCategory(Boolean.valueOf(false));
        setBuySellCategory(Boolean.valueOf(false));
        setFarmingBusinessCategory(Boolean.valueOf(false));
    }

    public Boolean getFarmingNewsCategory() {
        return this.farmingNewsCategory;
    }

    public void setFarmingNewsCategory(Boolean farmingNewsCategory) {
        this.farmingNewsCategory = farmingNewsCategory;
    }

    public void toggleFarmingNewsCategory() {
        if (getFarmingNewsCategory().booleanValue()) {
            setFarmingNewsCategory(Boolean.valueOf(false));
            if (!getFarmingNewsCategory().booleanValue() && !getDairyFarmingCategory().booleanValue() && !getCattleBreedingCategory().booleanValue() && !getBuySellCategory().booleanValue() && !getFarmingBusinessCategory().booleanValue()) {
                setAllCategory(Boolean.valueOf(true));
                return;
            }
            return;
        }
        setFarmingNewsCategory(Boolean.valueOf(true));
        setAllCategory(Boolean.valueOf(false));
    }

    public Boolean getDairyFarmingCategory() {
        return this.dairyFarmingCategory;
    }

    public void setDairyFarmingCategory(Boolean dairyFarmingCategory) {
        this.dairyFarmingCategory = dairyFarmingCategory;
    }

    public void toggleDairyFarmingCategor() {
        if (getDairyFarmingCategory().booleanValue()) {
            setDairyFarmingCategory(Boolean.valueOf(false));
            if (!getFarmingNewsCategory().booleanValue() && !getDairyFarmingCategory().booleanValue() && !getCattleBreedingCategory().booleanValue() && !getBuySellCategory().booleanValue() && !getFarmingBusinessCategory().booleanValue()) {
                setAllCategory(Boolean.valueOf(true));
                return;
            }
            return;
        }
        setDairyFarmingCategory(Boolean.valueOf(true));
        setAllCategory(Boolean.valueOf(false));
    }

    public Boolean getCattleBreedingCategory() {
        return this.cattleBreedingCategory;
    }

    public void setCattleBreedingCategory(Boolean cattleBreedingCategory) {
        this.cattleBreedingCategory = cattleBreedingCategory;
    }

    public void toggleCattleBreedingCategory() {
        if (getCattleBreedingCategory().booleanValue()) {
            setCattleBreedingCategory(Boolean.valueOf(false));
            if (!getFarmingNewsCategory().booleanValue() && !getDairyFarmingCategory().booleanValue() && !getCattleBreedingCategory().booleanValue() && !getBuySellCategory().booleanValue() && !getFarmingBusinessCategory().booleanValue()) {
                setAllCategory(Boolean.valueOf(true));
                return;
            }
            return;
        }
        setCattleBreedingCategory(Boolean.valueOf(true));
        setAllCategory(Boolean.valueOf(false));
    }

    public Boolean getBuySellCategory() {
        return this.buySellCategory;
    }

    public void setBuySellCategory(Boolean buySellCategory) {
        this.buySellCategory = buySellCategory;
    }

    public void toggleBuySellCategory() {
        if (getBuySellCategory().booleanValue()) {
            setBuySellCategory(Boolean.valueOf(false));
            if (!getFarmingNewsCategory().booleanValue() && !getDairyFarmingCategory().booleanValue() && !getCattleBreedingCategory().booleanValue() && !getBuySellCategory().booleanValue() && !getFarmingBusinessCategory().booleanValue()) {
                setAllCategory(Boolean.valueOf(true));
                return;
            }
            return;
        }
        setBuySellCategory(Boolean.valueOf(true));
        setAllCategory(Boolean.valueOf(false));
    }

    public Boolean getFarmingBusinessCategory() {
        return this.farmingBusinessCategory;
    }

    public void setFarmingBusinessCategory(Boolean farmingBusinessCategory) {
        this.farmingBusinessCategory = farmingBusinessCategory;
    }

    public void toggleFarmingBusinessCategory() {
        if (getFarmingBusinessCategory().booleanValue()) {
            setFarmingBusinessCategory(Boolean.valueOf(false));
            if (!getFarmingNewsCategory().booleanValue() && !getDairyFarmingCategory().booleanValue() && !getCattleBreedingCategory().booleanValue() && !getBuySellCategory().booleanValue() && !getFarmingBusinessCategory().booleanValue()) {
                setAllCategory(Boolean.valueOf(true));
                return;
            }
            return;
        }
        setFarmingBusinessCategory(Boolean.valueOf(true));
        setAllCategory(Boolean.valueOf(false));
    }

    public HashMap<Integer, String> getCategories() {
        return this.categories;
    }

    public void setCategories() {
        this.categories = new HashMap();
        this.categories.put(Integer.valueOf(0), this.activity.getString(C0530R.string.all));
        this.categories.put(Integer.valueOf(1), this.activity.getString(C0530R.string.farming_news));
        this.categories.put(Integer.valueOf(2), this.activity.getString(C0530R.string.dairy_farming));
        this.categories.put(Integer.valueOf(3), this.activity.getString(C0530R.string.cattle_breeding));
        this.categories.put(Integer.valueOf(4), this.activity.getString(C0530R.string.buy_sell));
        this.categories.put(Integer.valueOf(5), this.activity.getString(C0530R.string.farming_business));
    }

    public JSONObject getFilters() {
        try {
            JSONObject filter = new JSONObject();
            if (getAllUsers().booleanValue()) {
                filter.put("userFilter", null);
            } else if (getPeopleILiked().booleanValue()) {
                filter.put("userFilter", true);
            }
            filter.put("countryFilter", getCountry());
            if (getAllCategory().booleanValue()) {
                filter.put("categoryFilter", null);
                return filter;
            }
            JSONArray category = new JSONArray();
            if (getFarmingNewsCategory().booleanValue()) {
                category.put(1);
            }
            if (getDairyFarmingCategory().booleanValue()) {
                category.put(2);
            }
            if (getCattleBreedingCategory().booleanValue()) {
                category.put(3);
            }
            if (getBuySellCategory().booleanValue()) {
                category.put(4);
            }
            if (getFarmingBusinessCategory().booleanValue()) {
                category.put(5);
            }
            filter.put("categoryFilter", category);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        String text = "";
        if (this.activity == null) {
            return text;
        }
        if (getAllUsers().booleanValue()) {
            text = text + this.activity.getResources().getString(C0530R.string.all_users);
        } else {
            text = text + this.activity.getResources().getString(C0530R.string.people_i_liked);
        }
        text = text + StringUtils.SPACE + this.activity.getResources().getString(C0530R.string.from) + StringUtils.SPACE;
        if (getCountry() != null) {
            text = text + getCountry();
        } else {
            text = text + this.activity.getResources().getString(C0530R.string.all_countries);
        }
        text = text + StringUtils.SPACE + this.activity.getResources().getString(C0530R.string.interested_in) + StringUtils.SPACE;
        if (getAllCategory().booleanValue()) {
            text = text + this.activity.getResources().getString(C0530R.string.all_categories);
        } else {
            text = text + this.activity.getResources().getString(C0530R.string.the) + StringUtils.SPACE;
            if (getFarmingNewsCategory().booleanValue()) {
                text = text + this.activity.getResources().getString(C0530R.string.farming_news) + StringUtils.SPACE + this.activity.getString(C0530R.string.and) + StringUtils.SPACE;
            }
            if (getDairyFarmingCategory().booleanValue()) {
                text = text + this.activity.getResources().getString(C0530R.string.dairy_farming) + StringUtils.SPACE + this.activity.getString(C0530R.string.and) + StringUtils.SPACE;
            }
            if (getCattleBreedingCategory().booleanValue()) {
                text = text + this.activity.getResources().getString(C0530R.string.cattle_breeding) + StringUtils.SPACE + this.activity.getString(C0530R.string.and) + StringUtils.SPACE;
            }
            if (getBuySellCategory().booleanValue()) {
                text = text + this.activity.getResources().getString(C0530R.string.buy_sell) + StringUtils.SPACE + this.activity.getString(C0530R.string.and) + StringUtils.SPACE;
            }
            if (getFarmingBusinessCategory().booleanValue()) {
                text = text + this.activity.getResources().getString(C0530R.string.farming_business) + StringUtils.SPACE + this.activity.getString(C0530R.string.and) + StringUtils.SPACE;
            }
            text = text.substring(0, text.length() - 5);
        }
        return text + ".";
    }
}
