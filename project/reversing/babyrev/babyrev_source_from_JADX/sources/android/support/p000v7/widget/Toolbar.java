package android.support.p000v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.p000v7.app.ActionBar;
import android.support.p000v7.appcompat.C0046R;
import android.support.p000v7.content.res.AppCompatResources;
import android.support.p000v7.view.CollapsibleActionView;
import android.support.p000v7.view.SupportMenuInflater;
import android.support.p000v7.view.menu.MenuBuilder;
import android.support.p000v7.view.menu.MenuItemImpl;
import android.support.p000v7.view.menu.MenuPresenter;
import android.support.p000v7.view.menu.MenuView;
import android.support.p000v7.view.menu.SubMenuBuilder;
import android.support.p000v7.widget.ActionMenuView;
import android.support.p001v4.view.AbsSavedState;
import android.support.p001v4.view.GravityCompat;
import android.support.p001v4.view.MarginLayoutParamsCompat;
import android.support.p001v4.view.ViewCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/* renamed from: android.support.v7.widget.Toolbar */
public class Toolbar extends ViewGroup {
    private static final String TAG = "Toolbar";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    /* renamed from: android.support.v7.widget.Toolbar$OnMenuItemClickListener */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public Toolbar(Context context) {
        this(context, (AttributeSet) null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, C0046R.attr.toolbarStyle);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int i;
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<>();
        this.mHiddenViews = new ArrayList<>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (Toolbar.this.mOnMenuItemClickListener != null) {
                    return Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(item);
                }
                return false;
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable() {
            public void run() {
                Toolbar.this.showOverflowMenu();
            }
        };
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, C0046R.styleable.Toolbar, defStyleAttr, 0);
        this.mTitleTextAppearance = a.getResourceId(C0046R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = a.getResourceId(C0046R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = a.getInteger(C0046R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = a.getInteger(C0046R.styleable.Toolbar_buttonGravity, 48);
        int titleMargin = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_titleMargin, 0);
        titleMargin = a.hasValue(C0046R.styleable.Toolbar_titleMargins) ? a.getDimensionPixelOffset(C0046R.styleable.Toolbar_titleMargins, titleMargin) : titleMargin;
        this.mTitleMarginBottom = titleMargin;
        this.mTitleMarginTop = titleMargin;
        this.mTitleMarginEnd = titleMargin;
        this.mTitleMarginStart = titleMargin;
        int marginStart = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_titleMarginStart, -1);
        if (marginStart >= 0) {
            this.mTitleMarginStart = marginStart;
        }
        int marginEnd = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_titleMarginEnd, -1);
        if (marginEnd >= 0) {
            this.mTitleMarginEnd = marginEnd;
        }
        int marginTop = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_titleMarginTop, -1);
        if (marginTop >= 0) {
            this.mTitleMarginTop = marginTop;
        }
        int marginBottom = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_titleMarginBottom, -1);
        if (marginBottom >= 0) {
            this.mTitleMarginBottom = marginBottom;
        }
        this.mMaxButtonHeight = a.getDimensionPixelSize(C0046R.styleable.Toolbar_maxButtonHeight, -1);
        int contentInsetStart = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        int contentInsetEnd = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int contentInsetLeft = a.getDimensionPixelSize(C0046R.styleable.Toolbar_contentInsetLeft, 0);
        int contentInsetRight = a.getDimensionPixelSize(C0046R.styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        this.mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
        if (!(contentInsetStart == Integer.MIN_VALUE && contentInsetEnd == Integer.MIN_VALUE)) {
            this.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
        }
        this.mContentInsetStartWithNavigation = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = a.getDimensionPixelOffset(C0046R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = a.getDrawable(C0046R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = a.getText(C0046R.styleable.Toolbar_collapseContentDescription);
        CharSequence title = a.getText(C0046R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        CharSequence subtitle = a.getText(C0046R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(subtitle)) {
            setSubtitle(subtitle);
        }
        this.mPopupContext = getContext();
        int i2 = titleMargin;
        setPopupTheme(a.getResourceId(C0046R.styleable.Toolbar_popupTheme, 0));
        Drawable navIcon = a.getDrawable(C0046R.styleable.Toolbar_navigationIcon);
        if (navIcon != null) {
            setNavigationIcon(navIcon);
        }
        CharSequence navDesc = a.getText(C0046R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(navDesc)) {
            setNavigationContentDescription(navDesc);
        }
        Drawable drawable = navIcon;
        Drawable logo = a.getDrawable(C0046R.styleable.Toolbar_logo);
        if (logo != null) {
            setLogo(logo);
        }
        Drawable drawable2 = logo;
        CharSequence logoDesc = a.getText(C0046R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(logoDesc)) {
            setLogoDescription(logoDesc);
        }
        CharSequence charSequence = logoDesc;
        if (a.hasValue(C0046R.styleable.Toolbar_titleTextColor)) {
            CharSequence charSequence2 = navDesc;
            i = -1;
            setTitleTextColor(a.getColor(C0046R.styleable.Toolbar_titleTextColor, -1));
        } else {
            i = -1;
        }
        if (a.hasValue(C0046R.styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(a.getColor(C0046R.styleable.Toolbar_subtitleTextColor, i));
        }
        a.recycle();
    }

    public void setPopupTheme(@StyleRes int resId) {
        if (this.mPopupTheme != resId) {
            this.mPopupTheme = resId;
            if (resId == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public void setTitleMargin(int start, int top, int end, int bottom) {
        this.mTitleMarginStart = start;
        this.mTitleMarginTop = top;
        this.mTitleMarginEnd = end;
        this.mTitleMarginBottom = bottom;
        requestLayout();
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public void setTitleMarginStart(int margin) {
        this.mTitleMarginStart = margin;
        requestLayout();
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    public void setTitleMarginTop(int margin) {
        this.mTitleMarginTop = margin;
        requestLayout();
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public void setTitleMarginEnd(int margin) {
        this.mTitleMarginEnd = margin;
        requestLayout();
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public void setTitleMarginBottom(int margin) {
        this.mTitleMarginBottom = margin;
        requestLayout();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (Build.VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(layoutDirection);
        }
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (layoutDirection != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    public void setLogo(@DrawableRes int resId) {
        setLogo(AppCompatResources.getDrawable(getContext(), resId));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r1.mMenuView;
     */
    @android.support.annotation.RestrictTo({android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean canShowOverflowMenu() {
        /*
            r1 = this;
            int r0 = r1.getVisibility()
            if (r0 != 0) goto L_0x0012
            android.support.v7.widget.ActionMenuView r0 = r1.mMenuView
            if (r0 == 0) goto L_0x0012
            boolean r0 = r0.isOverflowReserved()
            if (r0 == 0) goto L_0x0012
            r0 = 1
            goto L_0x0013
        L_0x0012:
            r0 = 0
        L_0x0013:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v7.widget.Toolbar.canShowOverflowMenu():boolean");
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowPending();
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.hideOverflowMenu();
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setMenu(MenuBuilder menu, ActionMenuPresenter outerPresenter) {
        if (menu != null || this.mMenuView != null) {
            ensureMenuView();
            MenuBuilder oldMenu = this.mMenuView.peekMenu();
            if (oldMenu != menu) {
                if (oldMenu != null) {
                    oldMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
                    oldMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                if (this.mExpandedMenuPresenter == null) {
                    this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
                }
                outerPresenter.setExpandedActionViewsExclusive(true);
                if (menu != null) {
                    menu.addMenuPresenter(outerPresenter, this.mPopupContext);
                    menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
                } else {
                    outerPresenter.initForMenu(this.mPopupContext, (MenuBuilder) null);
                    this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, (MenuBuilder) null);
                    outerPresenter.updateMenuView(true);
                    this.mExpandedMenuPresenter.updateMenuView(true);
                }
                this.mMenuView.setPopupTheme(this.mPopupTheme);
                this.mMenuView.setPresenter(outerPresenter);
                this.mOuterActionMenuPresenter = outerPresenter;
            }
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isTitleTruncated() {
        Layout titleLayout;
        TextView textView = this.mTitleTextView;
        if (textView == null || (titleLayout = textView.getLayout()) == null) {
            return false;
        }
        int lineCount = titleLayout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (titleLayout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            ImageView imageView = this.mLogoView;
            if (imageView != null && isChildOrHidden(imageView)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        ImageView imageView2 = this.mLogoView;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    public void setLogoDescription(@StringRes int resId) {
        setLogoDescription(getContext().getText(resId));
    }

    public void setLogoDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(description);
        }
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(getContext());
        }
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl item = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (item != null) {
            item.collapseActionView();
        }
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                this.mTitleTextView = new AppCompatTextView(context);
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mTitleTextColor;
                if (i2 != 0) {
                    this.mTitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        } else {
            TextView textView = this.mTitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        }
        TextView textView2 = this.mTitleTextView;
        if (textView2 != null) {
            textView2.setText(title);
        }
        this.mTitleText = title;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public void setSubtitle(@StringRes int resId) {
        setSubtitle(getContext().getText(resId));
    }

    public void setSubtitle(CharSequence subtitle) {
        if (!TextUtils.isEmpty(subtitle)) {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                this.mSubtitleTextView = new AppCompatTextView(context);
                this.mSubtitleTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mSubtitleTextColor;
                if (i2 != 0) {
                    this.mSubtitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        } else {
            TextView textView = this.mSubtitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        }
        TextView textView2 = this.mSubtitleTextView;
        if (textView2 != null) {
            textView2.setText(subtitle);
        }
        this.mSubtitleText = subtitle;
    }

    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        this.mTitleTextAppearance = resId;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, resId);
        }
    }

    public void setSubtitleTextAppearance(Context context, @StyleRes int resId) {
        this.mSubtitleTextAppearance = resId;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, resId);
        }
    }

    public void setTitleTextColor(@ColorInt int color) {
        this.mTitleTextColor = color;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setSubtitleTextColor(@ColorInt int color) {
        this.mSubtitleTextColor = color;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    @Nullable
    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public void setNavigationContentDescription(@StringRes int resId) {
        setNavigationContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    public void setNavigationContentDescription(@Nullable CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(description);
        }
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        if (icon != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            ImageButton imageButton = this.mNavButtonView;
            if (imageButton != null && isChildOrHidden(imageButton)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        ImageButton imageButton2 = this.mNavButtonView;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(icon);
        }
    }

    @Nullable
    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    public void setNavigationOnClickListener(View.OnClickListener listener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(listener);
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public void setOverflowIcon(@Nullable Drawable icon) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(icon);
    }

    @Nullable
    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menu = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            this.mMenuView = new ActionMenuView(getContext());
            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = 8388613 | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(lp);
            addSystemView(this.mMenuView, false);
        }
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    public void inflateMenu(@MenuRes int resId) {
        getMenuInflater().inflate(resId, getMenu());
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setContentInsetsRelative(int contentInsetStart, int contentInsetEnd) {
        ensureContentInsets();
        this.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getStart();
        }
        return 0;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getEnd();
        }
        return 0;
    }

    public void setContentInsetsAbsolute(int contentInsetLeft, int contentInsetRight) {
        ensureContentInsets();
        this.mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getLeft();
        }
        return 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getRight();
        }
        return 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i = this.mContentInsetStartWithNavigation;
        return i != Integer.MIN_VALUE ? i : getContentInsetStart();
    }

    public void setContentInsetStartWithNavigation(int insetStartWithNavigation) {
        if (insetStartWithNavigation < 0) {
            insetStartWithNavigation = Integer.MIN_VALUE;
        }
        if (insetStartWithNavigation != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = insetStartWithNavigation;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getContentInsetEndWithActions() {
        int i = this.mContentInsetEndWithActions;
        return i != Integer.MIN_VALUE ? i : getContentInsetEnd();
    }

    public void setContentInsetEndWithActions(int insetEndWithActions) {
        if (insetEndWithActions < 0) {
            insetEndWithActions = Integer.MIN_VALUE;
        }
        if (insetEndWithActions != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = insetEndWithActions;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getCurrentContentInsetStart() {
        if (getNavigationIcon() != null) {
            return Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        return getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        boolean hasActions = false;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            MenuBuilder mb = actionMenuView.peekMenu();
            hasActions = mb != null && mb.hasVisibleItems();
        }
        if (hasActions) {
            return Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        return getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetEnd();
        }
        return getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        if (ViewCompat.getLayoutDirection(this) == 1) {
            return getCurrentContentInsetStart();
        }
        return getCurrentContentInsetEnd();
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), (AttributeSet) null, C0046R.attr.toolbarNavigationButtonStyle);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = 8388611 | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(lp);
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            this.mCollapseButtonView = new AppCompatImageButton(getContext(), (AttributeSet) null, C0046R.attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = 8388611 | (this.mButtonGravity & 112);
            lp.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(lp);
            this.mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toolbar.this.collapseActionView();
                }
            });
        }
    }

    private void addSystemView(View v, boolean allowHide) {
        LayoutParams lp;
        ViewGroup.LayoutParams vlp = v.getLayoutParams();
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        lp.mViewType = 1;
        if (!allowHide || this.mExpandedActionView == null) {
            addView(v, lp);
            return;
        }
        v.setLayoutParams(lp);
        this.mHiddenViews.add(v);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (!(expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null)) {
            state.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        state.isOverflowOpen = isOverflowMenuShowing();
        return state;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        MenuItem item;
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        ActionMenuView actionMenuView = this.mMenuView;
        Menu menu = actionMenuView != null ? actionMenuView.peekMenu() : null;
        if (!(ss.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null || menu == null || (item = menu.findItem(ss.expandedMenuItemId)) == null)) {
            item.expandActionView();
        }
        if (ss.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean handled = super.onTouchEvent(ev);
            if (action == 0 && !handled) {
                this.mEatingTouch = true;
            }
        }
        if (action == 1 || action == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    public boolean onHoverEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean handled = super.onHoverEvent(ev);
            if (action == 9 && !handled) {
                this.mEatingHover = true;
            }
        }
        if (action == 10 || action == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    private void measureChildConstrained(View child, int parentWidthSpec, int widthUsed, int parentHeightSpec, int heightUsed, int heightConstraint) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidthSpec = getChildMeasureSpec(parentWidthSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        int childHeightSpec = getChildMeasureSpec(parentHeightSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed, lp.height);
        int childHeightMode = View.MeasureSpec.getMode(childHeightSpec);
        if (childHeightMode != 1073741824 && heightConstraint >= 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(childHeightMode != 0 ? Math.min(View.MeasureSpec.getSize(childHeightSpec), heightConstraint) : heightConstraint, 1073741824);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private int measureChildCollapseMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int leftDiff = lp.leftMargin - collapsingMargins[0];
        int rightDiff = lp.rightMargin - collapsingMargins[1];
        int hMargins = Math.max(0, leftDiff) + Math.max(0, rightDiff);
        collapsingMargins[0] = Math.max(0, -leftDiff);
        collapsingMargins[1] = Math.max(0, -rightDiff);
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + hMargins + widthUsed, lp.width), getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed, lp.height));
        return child.getMeasuredWidth() + hMargins;
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (shouldLayout(child) && child.getMeasuredWidth() > 0 && child.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int marginEndIndex;
        int marginStartIndex;
        int marginStartIndex2;
        int childState;
        int height;
        int childState2;
        int childState3;
        int titleHeight;
        int menuWidth;
        int childCount;
        int menuWidth2;
        int height2 = 0;
        int childState4 = 0;
        int[] collapsingMargins = this.mTempMargins;
        if (ViewUtils.isLayoutRtl(this)) {
            marginStartIndex = 1;
            marginEndIndex = 0;
        } else {
            marginStartIndex = 0;
            marginEndIndex = 1;
        }
        int navWidth = 0;
        if (shouldLayout(this.mNavButtonView)) {
            measureChildConstrained(this.mNavButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, this.mMaxButtonHeight);
            navWidth = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
            height2 = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
            childState4 = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        }
        if (shouldLayout(this.mCollapseButtonView)) {
            measureChildConstrained(this.mCollapseButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, this.mMaxButtonHeight);
            navWidth = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
            height2 = Math.max(height2, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
            childState4 = View.combineMeasuredStates(childState4, this.mCollapseButtonView.getMeasuredState());
        }
        int contentInsetStart = getCurrentContentInsetStart();
        int width = 0 + Math.max(contentInsetStart, navWidth);
        collapsingMargins[marginStartIndex] = Math.max(0, contentInsetStart - navWidth);
        if (shouldLayout(this.mMenuView)) {
            char c = marginStartIndex;
            marginStartIndex2 = 0;
            measureChildConstrained(this.mMenuView, widthMeasureSpec, width, heightMeasureSpec, 0, this.mMaxButtonHeight);
            int menuWidth3 = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
            int height3 = Math.max(height2, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
            childState2 = View.combineMeasuredStates(childState4, this.mMenuView.getMeasuredState());
            childState = height3;
            height = menuWidth3;
        } else {
            marginStartIndex2 = 0;
            childState2 = childState4;
            childState = height2;
            height = 0;
        }
        int contentInsetEnd = getCurrentContentInsetEnd();
        int width2 = width + Math.max(contentInsetEnd, height);
        collapsingMargins[marginEndIndex] = Math.max(marginStartIndex2, contentInsetEnd - height);
        if (shouldLayout(this.mExpandedActionView)) {
            int i = contentInsetEnd;
            width2 += measureChildCollapseMargins(this.mExpandedActionView, widthMeasureSpec, width2, heightMeasureSpec, 0, collapsingMargins);
            childState = Math.max(childState, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
            childState3 = View.combineMeasuredStates(childState2, this.mExpandedActionView.getMeasuredState());
        } else {
            childState3 = childState2;
        }
        if (shouldLayout(this.mLogoView)) {
            width2 += measureChildCollapseMargins(this.mLogoView, widthMeasureSpec, width2, heightMeasureSpec, 0, collapsingMargins);
            childState = Math.max(childState, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
            childState3 = View.combineMeasuredStates(childState3, this.mLogoView.getMeasuredState());
        }
        int childCount2 = getChildCount();
        int height4 = childState;
        int width3 = width2;
        int i2 = 0;
        while (i2 < childCount2) {
            View child = getChildAt(i2);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.mViewType != 0) {
                View view = child;
                childCount = childCount2;
                menuWidth = height;
                menuWidth2 = height4;
            } else if (!shouldLayout(child)) {
                childCount = childCount2;
                menuWidth = height;
                menuWidth2 = height4;
            } else {
                LayoutParams layoutParams = lp;
                View child2 = child;
                menuWidth = height;
                childCount = childCount2;
                width3 += measureChildCollapseMargins(child, widthMeasureSpec, width3, heightMeasureSpec, 0, collapsingMargins);
                View child3 = child2;
                height4 = Math.max(height4, child2.getMeasuredHeight() + getVerticalMargins(child3));
                childState3 = View.combineMeasuredStates(childState3, child3.getMeasuredState());
                i2++;
                childCount2 = childCount;
                height = menuWidth;
            }
            height4 = menuWidth2;
            i2++;
            childCount2 = childCount;
            height = menuWidth;
        }
        int i3 = height;
        int height5 = height4;
        int titleWidth = 0;
        int titleHeight2 = 0;
        int titleVertMargins = this.mTitleMarginTop + this.mTitleMarginBottom;
        int titleHorizMargins = this.mTitleMarginStart + this.mTitleMarginEnd;
        if (shouldLayout(this.mTitleTextView)) {
            int measureChildCollapseMargins = measureChildCollapseMargins(this.mTitleTextView, widthMeasureSpec, width3 + titleHorizMargins, heightMeasureSpec, titleVertMargins, collapsingMargins);
            titleWidth = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
            titleHeight2 = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
            childState3 = View.combineMeasuredStates(childState3, this.mTitleTextView.getMeasuredState());
        }
        if (shouldLayout(this.mSubtitleTextView)) {
            titleWidth = Math.max(titleWidth, measureChildCollapseMargins(this.mSubtitleTextView, widthMeasureSpec, width3 + titleHorizMargins, heightMeasureSpec, titleHeight2 + titleVertMargins, collapsingMargins));
            int titleHeight3 = titleHeight2 + this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
            childState3 = View.combineMeasuredStates(childState3, this.mSubtitleTextView.getMeasuredState());
            titleHeight = titleHeight3;
        } else {
            titleHeight = titleHeight2;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(width3 + titleWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, -16777216 & childState3), shouldCollapse() ? 0 : View.resolveSizeAndState(Math.max(Math.max(height5, titleHeight) + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, childState3 << 16));
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x015e, code lost:
        if (r0.mTitleTextView.getMeasuredWidth() <= 0) goto L_0x0163;
     */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x017c  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01d2  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01e3  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0263  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayout(boolean r34, int r35, int r36, int r37, int r38) {
        /*
            r33 = this;
            r0 = r33
            int r1 = android.support.p001v4.view.ViewCompat.getLayoutDirection(r33)
            r2 = 1
            r3 = 0
            if (r1 != r2) goto L_0x000c
            r1 = 1
            goto L_0x000d
        L_0x000c:
            r1 = 0
        L_0x000d:
            int r4 = r33.getWidth()
            int r5 = r33.getHeight()
            int r6 = r33.getPaddingLeft()
            int r7 = r33.getPaddingRight()
            int r8 = r33.getPaddingTop()
            int r9 = r33.getPaddingBottom()
            r10 = r6
            int r11 = r4 - r7
            int[] r12 = r0.mTempMargins
            r12[r2] = r3
            r12[r3] = r3
            int r13 = android.support.p001v4.view.ViewCompat.getMinimumHeight(r33)
            if (r13 < 0) goto L_0x003b
            int r14 = r38 - r36
            int r14 = java.lang.Math.min(r13, r14)
            goto L_0x003c
        L_0x003b:
            r14 = 0
        L_0x003c:
            android.widget.ImageButton r15 = r0.mNavButtonView
            boolean r15 = r0.shouldLayout(r15)
            if (r15 == 0) goto L_0x0053
            if (r1 == 0) goto L_0x004d
            android.widget.ImageButton r15 = r0.mNavButtonView
            int r11 = r0.layoutChildRight(r15, r11, r12, r14)
            goto L_0x0053
        L_0x004d:
            android.widget.ImageButton r15 = r0.mNavButtonView
            int r10 = r0.layoutChildLeft(r15, r10, r12, r14)
        L_0x0053:
            android.widget.ImageButton r15 = r0.mCollapseButtonView
            boolean r15 = r0.shouldLayout(r15)
            if (r15 == 0) goto L_0x006a
            if (r1 == 0) goto L_0x0064
            android.widget.ImageButton r15 = r0.mCollapseButtonView
            int r11 = r0.layoutChildRight(r15, r11, r12, r14)
            goto L_0x006a
        L_0x0064:
            android.widget.ImageButton r15 = r0.mCollapseButtonView
            int r10 = r0.layoutChildLeft(r15, r10, r12, r14)
        L_0x006a:
            android.support.v7.widget.ActionMenuView r15 = r0.mMenuView
            boolean r15 = r0.shouldLayout(r15)
            if (r15 == 0) goto L_0x0081
            if (r1 == 0) goto L_0x007b
            android.support.v7.widget.ActionMenuView r15 = r0.mMenuView
            int r10 = r0.layoutChildLeft(r15, r10, r12, r14)
            goto L_0x0081
        L_0x007b:
            android.support.v7.widget.ActionMenuView r15 = r0.mMenuView
            int r11 = r0.layoutChildRight(r15, r11, r12, r14)
        L_0x0081:
            int r15 = r33.getCurrentContentInsetLeft()
            int r16 = r33.getCurrentContentInsetRight()
            int r2 = r15 - r10
            int r2 = java.lang.Math.max(r3, r2)
            r12[r3] = r2
            int r2 = r4 - r7
            int r2 = r2 - r11
            int r2 = r16 - r2
            int r2 = java.lang.Math.max(r3, r2)
            r17 = 1
            r12[r17] = r2
            int r2 = java.lang.Math.max(r10, r15)
            int r10 = r4 - r7
            int r10 = r10 - r16
            int r10 = java.lang.Math.min(r11, r10)
            android.view.View r11 = r0.mExpandedActionView
            boolean r11 = r0.shouldLayout(r11)
            if (r11 == 0) goto L_0x00c1
            if (r1 == 0) goto L_0x00bb
            android.view.View r11 = r0.mExpandedActionView
            int r10 = r0.layoutChildRight(r11, r10, r12, r14)
            goto L_0x00c1
        L_0x00bb:
            android.view.View r11 = r0.mExpandedActionView
            int r2 = r0.layoutChildLeft(r11, r2, r12, r14)
        L_0x00c1:
            android.widget.ImageView r11 = r0.mLogoView
            boolean r11 = r0.shouldLayout(r11)
            if (r11 == 0) goto L_0x00d8
            if (r1 == 0) goto L_0x00d2
            android.widget.ImageView r11 = r0.mLogoView
            int r10 = r0.layoutChildRight(r11, r10, r12, r14)
            goto L_0x00d8
        L_0x00d2:
            android.widget.ImageView r11 = r0.mLogoView
            int r2 = r0.layoutChildLeft(r11, r2, r12, r14)
        L_0x00d8:
            android.widget.TextView r11 = r0.mTitleTextView
            boolean r11 = r0.shouldLayout(r11)
            android.widget.TextView r3 = r0.mSubtitleTextView
            boolean r3 = r0.shouldLayout(r3)
            r19 = 0
            if (r11 == 0) goto L_0x0105
            r20 = r13
            android.widget.TextView r13 = r0.mTitleTextView
            android.view.ViewGroup$LayoutParams r13 = r13.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r13 = (android.support.p000v7.widget.Toolbar.LayoutParams) r13
            r21 = r15
            int r15 = r13.topMargin
            r22 = r7
            android.widget.TextView r7 = r0.mTitleTextView
            int r7 = r7.getMeasuredHeight()
            int r15 = r15 + r7
            int r7 = r13.bottomMargin
            int r15 = r15 + r7
            int r19 = r19 + r15
            goto L_0x010b
        L_0x0105:
            r22 = r7
            r20 = r13
            r21 = r15
        L_0x010b:
            if (r3 == 0) goto L_0x0123
            android.widget.TextView r7 = r0.mSubtitleTextView
            android.view.ViewGroup$LayoutParams r7 = r7.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r7 = (android.support.p000v7.widget.Toolbar.LayoutParams) r7
            int r13 = r7.topMargin
            android.widget.TextView r15 = r0.mSubtitleTextView
            int r15 = r15.getMeasuredHeight()
            int r13 = r13 + r15
            int r15 = r7.bottomMargin
            int r13 = r13 + r15
            int r19 = r19 + r13
        L_0x0123:
            if (r11 != 0) goto L_0x0136
            if (r3 == 0) goto L_0x0128
            goto L_0x0136
        L_0x0128:
            r27 = r1
            r29 = r2
            r25 = r4
            r30 = r5
            r26 = r6
            r28 = r14
            goto L_0x02e0
        L_0x0136:
            if (r11 == 0) goto L_0x013b
            android.widget.TextView r7 = r0.mTitleTextView
            goto L_0x013d
        L_0x013b:
            android.widget.TextView r7 = r0.mSubtitleTextView
        L_0x013d:
            if (r3 == 0) goto L_0x0142
            android.widget.TextView r13 = r0.mSubtitleTextView
            goto L_0x0144
        L_0x0142:
            android.widget.TextView r13 = r0.mTitleTextView
        L_0x0144:
            android.view.ViewGroup$LayoutParams r15 = r7.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r15 = (android.support.p000v7.widget.Toolbar.LayoutParams) r15
            android.view.ViewGroup$LayoutParams r23 = r13.getLayoutParams()
            r24 = r7
            r7 = r23
            android.support.v7.widget.Toolbar$LayoutParams r7 = (android.support.p000v7.widget.Toolbar.LayoutParams) r7
            if (r11 == 0) goto L_0x0161
            r23 = r13
            android.widget.TextView r13 = r0.mTitleTextView
            int r13 = r13.getMeasuredWidth()
            if (r13 > 0) goto L_0x016d
            goto L_0x0163
        L_0x0161:
            r23 = r13
        L_0x0163:
            if (r3 == 0) goto L_0x016f
            android.widget.TextView r13 = r0.mSubtitleTextView
            int r13 = r13.getMeasuredWidth()
            if (r13 <= 0) goto L_0x016f
        L_0x016d:
            r13 = 1
            goto L_0x0170
        L_0x016f:
            r13 = 0
        L_0x0170:
            r25 = r4
            int r4 = r0.mGravity
            r4 = r4 & 112(0x70, float:1.57E-43)
            r26 = r6
            r6 = 48
            if (r4 == r6) goto L_0x01d2
            r6 = 80
            if (r4 == r6) goto L_0x01bd
            int r4 = r5 - r8
            int r4 = r4 - r9
            int r6 = r4 - r19
            int r6 = r6 / 2
            r27 = r4
            int r4 = r15.topMargin
            r28 = r14
            int r14 = r0.mTitleMarginTop
            int r4 = r4 + r14
            if (r6 >= r4) goto L_0x019c
            int r4 = r15.topMargin
            int r14 = r0.mTitleMarginTop
            int r6 = r4 + r14
            r29 = r2
            r14 = 0
            goto L_0x01ba
        L_0x019c:
            int r4 = r5 - r9
            int r4 = r4 - r19
            int r4 = r4 - r6
            int r4 = r4 - r8
            int r14 = r15.bottomMargin
            r29 = r2
            int r2 = r0.mTitleMarginBottom
            int r14 = r14 + r2
            if (r4 >= r14) goto L_0x01b9
            int r2 = r7.bottomMargin
            int r14 = r0.mTitleMarginBottom
            int r2 = r2 + r14
            int r2 = r2 - r4
            int r2 = r6 - r2
            r14 = 0
            int r6 = java.lang.Math.max(r14, r2)
            goto L_0x01ba
        L_0x01b9:
            r14 = 0
        L_0x01ba:
            int r2 = r8 + r6
            goto L_0x01e1
        L_0x01bd:
            r29 = r2
            r28 = r14
            r14 = 0
            r2 = r14
            r4 = r14
            r6 = r14
            int r14 = r5 - r9
            r27 = r2
            int r2 = r7.bottomMargin
            int r14 = r14 - r2
            int r2 = r0.mTitleMarginBottom
            int r14 = r14 - r2
            int r2 = r14 - r19
            goto L_0x01e1
        L_0x01d2:
            r29 = r2
            r28 = r14
            int r2 = r33.getPaddingTop()
            int r4 = r15.topMargin
            int r2 = r2 + r4
            int r4 = r0.mTitleMarginTop
            int r2 = r2 + r4
        L_0x01e1:
            if (r1 == 0) goto L_0x0263
            if (r13 == 0) goto L_0x01e8
            int r4 = r0.mTitleMarginStart
            goto L_0x01e9
        L_0x01e8:
            r4 = 0
        L_0x01e9:
            r6 = 1
            r14 = r12[r6]
            int r4 = r4 - r14
            r14 = 0
            int r17 = java.lang.Math.max(r14, r4)
            int r10 = r10 - r17
            r27 = r1
            int r1 = -r4
            int r1 = java.lang.Math.max(r14, r1)
            r12[r6] = r1
            r1 = r10
            r6 = r10
            if (r11 == 0) goto L_0x022c
            android.widget.TextView r14 = r0.mTitleTextView
            android.view.ViewGroup$LayoutParams r14 = r14.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r14 = (android.support.p000v7.widget.Toolbar.LayoutParams) r14
            r18 = r4
            android.widget.TextView r4 = r0.mTitleTextView
            int r4 = r4.getMeasuredWidth()
            int r4 = r1 - r4
            r30 = r5
            android.widget.TextView r5 = r0.mTitleTextView
            int r5 = r5.getMeasuredHeight()
            int r5 = r5 + r2
            r31 = r7
            android.widget.TextView r7 = r0.mTitleTextView
            r7.layout(r4, r2, r1, r5)
            int r7 = r0.mTitleMarginEnd
            int r1 = r4 - r7
            int r7 = r14.bottomMargin
            int r2 = r5 + r7
            goto L_0x0232
        L_0x022c:
            r18 = r4
            r30 = r5
            r31 = r7
        L_0x0232:
            if (r3 == 0) goto L_0x025a
            android.widget.TextView r4 = r0.mSubtitleTextView
            android.view.ViewGroup$LayoutParams r4 = r4.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r4 = (android.support.p000v7.widget.Toolbar.LayoutParams) r4
            int r5 = r4.topMargin
            int r2 = r2 + r5
            android.widget.TextView r5 = r0.mSubtitleTextView
            int r5 = r5.getMeasuredWidth()
            int r5 = r6 - r5
            android.widget.TextView r7 = r0.mSubtitleTextView
            int r7 = r7.getMeasuredHeight()
            int r7 = r7 + r2
            android.widget.TextView r14 = r0.mSubtitleTextView
            r14.layout(r5, r2, r6, r7)
            int r14 = r0.mTitleMarginEnd
            int r6 = r6 - r14
            int r14 = r4.bottomMargin
            int r2 = r7 + r14
        L_0x025a:
            if (r13 == 0) goto L_0x0261
            int r4 = java.lang.Math.min(r1, r6)
            r10 = r4
        L_0x0261:
            goto L_0x02e0
        L_0x0263:
            r27 = r1
            r30 = r5
            r31 = r7
            if (r13 == 0) goto L_0x026e
            int r1 = r0.mTitleMarginStart
            goto L_0x026f
        L_0x026e:
            r1 = 0
        L_0x026f:
            r4 = 0
            r5 = r12[r4]
            int r1 = r1 - r5
            int r5 = java.lang.Math.max(r4, r1)
            int r5 = r29 + r5
            int r6 = -r1
            int r6 = java.lang.Math.max(r4, r6)
            r12[r4] = r6
            r4 = r5
            r6 = r5
            if (r11 == 0) goto L_0x02ac
            android.widget.TextView r7 = r0.mTitleTextView
            android.view.ViewGroup$LayoutParams r7 = r7.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r7 = (android.support.p000v7.widget.Toolbar.LayoutParams) r7
            android.widget.TextView r14 = r0.mTitleTextView
            int r14 = r14.getMeasuredWidth()
            int r14 = r14 + r4
            r18 = r1
            android.widget.TextView r1 = r0.mTitleTextView
            int r1 = r1.getMeasuredHeight()
            int r1 = r1 + r2
            r29 = r5
            android.widget.TextView r5 = r0.mTitleTextView
            r5.layout(r4, r2, r14, r1)
            int r5 = r0.mTitleMarginEnd
            int r4 = r14 + r5
            int r5 = r7.bottomMargin
            int r2 = r1 + r5
            goto L_0x02b0
        L_0x02ac:
            r18 = r1
            r29 = r5
        L_0x02b0:
            if (r3 == 0) goto L_0x02d8
            android.widget.TextView r1 = r0.mSubtitleTextView
            android.view.ViewGroup$LayoutParams r1 = r1.getLayoutParams()
            android.support.v7.widget.Toolbar$LayoutParams r1 = (android.support.p000v7.widget.Toolbar.LayoutParams) r1
            int r5 = r1.topMargin
            int r2 = r2 + r5
            android.widget.TextView r5 = r0.mSubtitleTextView
            int r5 = r5.getMeasuredWidth()
            int r5 = r5 + r6
            android.widget.TextView r7 = r0.mSubtitleTextView
            int r7 = r7.getMeasuredHeight()
            int r7 = r7 + r2
            android.widget.TextView r14 = r0.mSubtitleTextView
            r14.layout(r6, r2, r5, r7)
            int r14 = r0.mTitleMarginEnd
            int r6 = r5 + r14
            int r14 = r1.bottomMargin
            int r2 = r7 + r14
        L_0x02d8:
            if (r13 == 0) goto L_0x02e0
            int r1 = java.lang.Math.max(r4, r6)
            r29 = r1
        L_0x02e0:
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            r2 = 3
            r0.addCustomViewsWithGravity(r1, r2)
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            int r1 = r1.size()
            r2 = 0
            r4 = r29
        L_0x02ef:
            if (r2 >= r1) goto L_0x0302
            java.util.ArrayList<android.view.View> r5 = r0.mTempViews
            java.lang.Object r5 = r5.get(r2)
            android.view.View r5 = (android.view.View) r5
            r6 = r28
            int r4 = r0.layoutChildLeft(r5, r4, r12, r6)
            int r2 = r2 + 1
            goto L_0x02ef
        L_0x0302:
            r6 = r28
            java.util.ArrayList<android.view.View> r2 = r0.mTempViews
            r5 = 5
            r0.addCustomViewsWithGravity(r2, r5)
            java.util.ArrayList<android.view.View> r2 = r0.mTempViews
            int r2 = r2.size()
            r5 = 0
        L_0x0311:
            if (r5 >= r2) goto L_0x0322
            java.util.ArrayList<android.view.View> r7 = r0.mTempViews
            java.lang.Object r7 = r7.get(r5)
            android.view.View r7 = (android.view.View) r7
            int r10 = r0.layoutChildRight(r7, r10, r12, r6)
            int r5 = r5 + 1
            goto L_0x0311
        L_0x0322:
            java.util.ArrayList<android.view.View> r5 = r0.mTempViews
            r7 = 1
            r0.addCustomViewsWithGravity(r5, r7)
            java.util.ArrayList<android.view.View> r5 = r0.mTempViews
            int r5 = r0.getViewListMeasuredWidth(r5, r12)
            int r7 = r25 - r26
            int r7 = r7 - r22
            int r7 = r7 / 2
            int r7 = r26 + r7
            int r13 = r5 / 2
            int r14 = r7 - r13
            int r15 = r14 + r5
            if (r14 >= r4) goto L_0x0340
            r14 = r4
            goto L_0x0346
        L_0x0340:
            if (r15 <= r10) goto L_0x0346
            int r17 = r15 - r10
            int r14 = r14 - r17
        L_0x0346:
            r17 = r1
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            int r1 = r1.size()
            r18 = 0
            r32 = r18
            r18 = r2
            r2 = r14
            r14 = r32
        L_0x0357:
            if (r14 >= r1) goto L_0x036c
            r23 = r1
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            java.lang.Object r1 = r1.get(r14)
            android.view.View r1 = (android.view.View) r1
            int r2 = r0.layoutChildLeft(r1, r2, r12, r6)
            int r14 = r14 + 1
            r1 = r23
            goto L_0x0357
        L_0x036c:
            r23 = r1
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            r1.clear()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v7.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    private int getViewListMeasuredWidth(List<View> views, int[] collapsingMargins) {
        int collapseLeft = collapsingMargins[0];
        int collapseRight = collapsingMargins[1];
        int width = 0;
        int count = views.size();
        for (int i = 0; i < count; i++) {
            View v = views.get(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            int l = lp.leftMargin - collapseLeft;
            int r = lp.rightMargin - collapseRight;
            int leftMargin = Math.max(0, l);
            int rightMargin = Math.max(0, r);
            collapseLeft = Math.max(0, -l);
            collapseRight = Math.max(0, -r);
            width += v.getMeasuredWidth() + leftMargin + rightMargin;
        }
        return width;
    }

    private int layoutChildLeft(View child, int left, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int l = lp.leftMargin - collapsingMargins[0];
        int left2 = left + Math.max(0, l);
        collapsingMargins[0] = Math.max(0, -l);
        int top = getChildTop(child, alignmentHeight);
        int childWidth = child.getMeasuredWidth();
        child.layout(left2, top, left2 + childWidth, child.getMeasuredHeight() + top);
        return left2 + lp.rightMargin + childWidth;
    }

    private int layoutChildRight(View child, int right, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int r = lp.rightMargin - collapsingMargins[1];
        int right2 = right - Math.max(0, r);
        collapsingMargins[1] = Math.max(0, -r);
        int top = getChildTop(child, alignmentHeight);
        int childWidth = child.getMeasuredWidth();
        child.layout(right2 - childWidth, top, right2, child.getMeasuredHeight() + top);
        return right2 - (lp.leftMargin + childWidth);
    }

    private int getChildTop(View child, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeight = child.getMeasuredHeight();
        int alignmentOffset = alignmentHeight > 0 ? (childHeight - alignmentHeight) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(lp.gravity);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - alignmentOffset;
        }
        if (childVerticalGravity == 80) {
            return (((getHeight() - getPaddingBottom()) - childHeight) - lp.bottomMargin) - alignmentOffset;
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int spaceAbove = (((height - paddingTop) - paddingBottom) - childHeight) / 2;
        if (spaceAbove < lp.topMargin) {
            spaceAbove = lp.topMargin;
        } else {
            int spaceBelow = (((height - paddingBottom) - childHeight) - spaceAbove) - paddingTop;
            if (spaceBelow < lp.bottomMargin) {
                spaceAbove = Math.max(0, spaceAbove - (lp.bottomMargin - spaceBelow));
            }
        }
        return paddingTop + spaceAbove;
    }

    private int getChildVerticalGravity(int gravity) {
        int vgrav = gravity & 112;
        if (vgrav == 16 || vgrav == 48 || vgrav == 80) {
            return vgrav;
        }
        return this.mGravity & 112;
    }

    private void addCustomViewsWithGravity(List<View> views, int gravity) {
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this) != 1) {
            z = false;
        }
        boolean isRtl = z;
        int childCount = getChildCount();
        int absGrav = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
        views.clear();
        if (isRtl) {
            for (int i = childCount - 1; i >= 0; i--) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.mViewType == 0 && shouldLayout(child) && getChildHorizontalGravity(lp.gravity) == absGrav) {
                    views.add(child);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            View child2 = getChildAt(i2);
            LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
            if (lp2.mViewType == 0 && shouldLayout(child2) && getChildHorizontalGravity(lp2.gravity) == absGrav) {
                views.add(child2);
            }
        }
    }

    private int getChildHorizontalGravity(int gravity) {
        int ld = ViewCompat.getLayoutDirection(this);
        int hGrav = GravityCompat.getAbsoluteGravity(gravity, ld) & 7;
        if (hGrav == 1 || hGrav == 3 || hGrav == 5) {
            return hGrav;
        }
        if (ld == 1) {
            return 5;
        }
        return 3;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private int getHorizontalMargins(View v) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(mlp) + MarginLayoutParamsCompat.getMarginEnd(mlp);
    }

    private int getVerticalMargins(View v) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return mlp.topMargin + mlp.bottomMargin;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        if (p instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams) p);
        }
        if (p instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    private static boolean isCustomView(View child) {
        return ((LayoutParams) child.getLayoutParams()).mViewType == 0;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    /* access modifiers changed from: package-private */
    public void removeChildrenForExpandedActionView() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (!(((LayoutParams) child.getLayoutParams()).mViewType == 2 || child == this.mMenuView)) {
                removeViewAt(i);
                this.mHiddenViews.add(child);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; i--) {
            addView(this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }

    private boolean isChildOrHidden(View child) {
        return child.getParent() == this || this.mHiddenViews.contains(child);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setCollapsible(boolean collapsible) {
        this.mCollapsible = collapsible;
        requestLayout();
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(MenuPresenter.Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.setMenuCallbacks(pcb, mcb);
        }
    }

    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }

    /* access modifiers changed from: package-private */
    public ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

    /* access modifiers changed from: package-private */
    public Context getPopupContext() {
        return this.mPopupContext;
    }

    /* renamed from: android.support.v7.widget.Toolbar$LayoutParams */
    public static class LayoutParams extends ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;

        public LayoutParams(@NonNull Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mViewType = 0;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(-2, -1, gravity);
        }

        public LayoutParams(LayoutParams source) {
            super((ActionBar.LayoutParams) source);
            this.mViewType = 0;
            this.mViewType = source.mViewType;
        }

        public LayoutParams(ActionBar.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super((ViewGroup.LayoutParams) source);
            this.mViewType = 0;
            copyMarginsFromCompat(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        /* access modifiers changed from: package-private */
        public void copyMarginsFromCompat(ViewGroup.MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
        }
    }

    /* renamed from: android.support.v7.widget.Toolbar$SavedState */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int expandedMenuItemId;
        boolean isOverflowOpen;

        public SavedState(Parcel source) {
            this(source, (ClassLoader) null);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.expandedMenuItemId = source.readInt();
            this.isOverflowOpen = source.readInt() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandedMenuItemId);
            out.writeInt(this.isOverflowOpen ? 1 : 0);
        }
    }

    /* renamed from: android.support.v7.widget.Toolbar$ExpandedActionViewMenuPresenter */
    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        ExpandedActionViewMenuPresenter() {
        }

        public void initForMenu(Context context, MenuBuilder menu) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder = this.mMenu;
            if (!(menuBuilder == null || (menuItemImpl = this.mCurrentExpandedItem) == null)) {
                menuBuilder.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menu;
        }

        public MenuView getMenuView(ViewGroup root) {
            return null;
        }

        public void updateMenuView(boolean cleared) {
            if (this.mCurrentExpandedItem != null) {
                boolean found = false;
                MenuBuilder menuBuilder = this.mMenu;
                if (menuBuilder != null) {
                    int count = menuBuilder.size();
                    int i = 0;
                    while (true) {
                        if (i >= count) {
                            break;
                        } else if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            found = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (!found) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }

        public void setCallback(MenuPresenter.Callback cb) {
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
            return false;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean flagActionItems() {
            return false;
        }

        public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
            Toolbar.this.ensureCollapseButtonView();
            ViewParent collapseButtonParent = Toolbar.this.mCollapseButtonView.getParent();
            Toolbar toolbar = Toolbar.this;
            if (collapseButtonParent != toolbar) {
                if (collapseButtonParent instanceof ViewGroup) {
                    ((ViewGroup) collapseButtonParent).removeView(toolbar.mCollapseButtonView);
                }
                Toolbar toolbar2 = Toolbar.this;
                toolbar2.addView(toolbar2.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = item.getActionView();
            this.mCurrentExpandedItem = item;
            ViewParent expandedActionParent = Toolbar.this.mExpandedActionView.getParent();
            Toolbar toolbar3 = Toolbar.this;
            if (expandedActionParent != toolbar3) {
                if (expandedActionParent instanceof ViewGroup) {
                    ((ViewGroup) expandedActionParent).removeView(toolbar3.mExpandedActionView);
                }
                LayoutParams lp = Toolbar.this.generateDefaultLayoutParams();
                lp.gravity = 8388611 | (Toolbar.this.mButtonGravity & 112);
                lp.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams(lp);
                Toolbar toolbar4 = Toolbar.this;
                toolbar4.addView(toolbar4.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mExpandedActionView);
            Toolbar toolbar2 = Toolbar.this;
            toolbar2.removeView(toolbar2.mCollapseButtonView);
            Toolbar toolbar3 = Toolbar.this;
            toolbar3.mExpandedActionView = null;
            toolbar3.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(false);
            return true;
        }

        public int getId() {
            return 0;
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }
    }
}
