/*
 * Copyright 2013 str4d
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tech.freak.wizardpager.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single page in the wizard.
 */
public abstract class Page implements PageTreeNode {
    /**
     * The key into {@link #getData()} used for wizards with simple (single) values.
     */
    public static final String SIMPLE_DATA_KEY = "_";

    protected ModelCallbacks mCallbacks;

    /**
     * Conditionals that rely on this page.
     */
    protected List<ModelCallbacks> mConditionals = new ArrayList<ModelCallbacks>();

    /**
     * Conditions on whether this page should be used.
     */
    protected List<Conditional.Condition> mConditions = new ArrayList<Conditional.Condition>();
    /**
     * Should all conditions be satisfied, or any of them?
     */
    protected boolean mConditionAnd = false;
    /**
     * The last condition status.
     */
    protected boolean mSatisfied = true;

    /**
     * Current wizard values/selections.
     */
    protected Bundle mData = new Bundle();
    protected String mTitle;
    protected boolean mRequired = false;
    protected String mParentKey;

    protected Page(ModelCallbacks callbacks, String title) {
        mCallbacks = callbacks;
        mTitle = title;
    }

    public Bundle getData() {
        return mData;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSatisfied() {
        boolean ret = true;
        if (mConditions.size() > 0) {
            ret = false;
            for (Conditional.Condition c : mConditions) {
                if (c.isSatisfied()) {
                    ret = true;
                    if (!mConditionAnd) break;
                } else if (mConditionAnd) {
                    ret = false;
                    break;
                }
            }
        }
        // If the conditions have changed, update the page tree.
        if (!(mSatisfied == ret)) {
            mSatisfied = ret;
            mCallbacks.onPageTreeChanged();
        }
        return mSatisfied;
    }

    public boolean isRequired() {
        return isSatisfied() && mRequired;
    }

    void setParentKey(String parentKey) {
        mParentKey = parentKey;
    }

    public Page findByKey(String key) {
        return getKey().equals(key) ? this : null;
    }

    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        if (isSatisfied())
            dest.add(this);
    }

    public abstract Fragment createFragment();

    public String getKey() {
        return (mParentKey != null) ? mParentKey + ":" + mTitle : mTitle;
    }

    public abstract void getReviewItems(ArrayList<ReviewItem> dest);

    public boolean isCompleted() {
        return true;
    }

    public void resetData(Bundle data) {
        mData = data;
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        for (ModelCallbacks c : mConditionals) {
            c.onPageDataChanged(this);
        }
        mCallbacks.onPageDataChanged(this);
    }

    public Page setRequired(boolean required) {
        mRequired = required;
        return this;
    }

    public Page makeConditional(Conditional conditional) {
        mConditionals.add(conditional);
        return this;
    }

    public <T> Page setEqualCondition(Conditional conditional, T comp) {
        Conditional.Condition c = conditional.new EqualCondition<T>(this, comp);
        mConditions.add(c);
        return this;
    }

    public Page satisfyAllConditions(boolean conditionAnd) {
        mConditionAnd = conditionAnd;
        return this;
    }
}
