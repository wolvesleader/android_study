package com.quincy.cancelviewpagercache;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup{
	private int horizontalSpacing = 22;//水平间距
	private int verticalSpacing = 12;//垂直间距，行与行之间的间距

	private ArrayList<Line> lineList = new ArrayList<FlowLayout.Line>();//用于存放每一行
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowLayout(Context context) {
		super(context);
	}
	
	@SuppressLint("DrawAllocation") @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		lineList.clear();//首先清除lineList
		
		int width = MeasureSpec.getSize(widthMeasureSpec);//获取控件的宽度
		//用于比较的宽度是不包含paddingLeft和paddingRight
		int unpaddingWidth = width - getPaddingLeft()-getPaddingRight();
		
		Line line = null;
		//遍历所有的子view,进行分行
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			//为了保证能够获取childView的宽高，手动测量
			childView.measure(0, 0);
			
			if(line==null){
				line = new Line();//用于存放子view
			}
			
			//1.如果line中一个view都没有，则直接添加，目的是为了保证每行至少有一个view
			if(line.getViewList().size()==0){
				line.add(childView);
				if(i==(getChildCount()-1)){
					lineList.add(line);
				}
			}else {
				//2.判断当前line的width+childView的宽+horizontalSpacing是否大于unpaddingWidth
				if(line.getWidth()+horizontalSpacing+childView.getMeasuredWidth()>unpaddingWidth){
					//换行，同时先存放当前line，再重新new Line
					lineList.add(line);
					
					line = new Line();
					line.add(childView);
					
					//如果当前childView是最后一个子view，则需要把line存放到lineList,否则会造成最后的line丢失
					if(i==(getChildCount()-1)){
						lineList.add(line);
					}
				}else {
					//属于这一行，则直接加入进来
					line.add(childView);
					
					//如果当前childView是最后一个子view，则需要把line存放到lineList,否则会造成最后的line丢失
					if(i==(getChildCount()-1)){
						lineList.add(line);
					}
				}
			}
		}
		line = null;//如果你的line是成员变量，则一定将line置为空
		
		//为所有的line申请高度
		int height = getPaddingTop()+getPaddingBottom();//加上paddingTop和paddingBottom
		for (int i = 0; i < lineList.size(); i++) {
			height += lineList.get(i).getHeight();//加上所有line的高度
		}
		height += (lineList.size()-1)*verticalSpacing;//加上所有垂直间距的高度
		
		//LogUtil.e(this, "height: "+height  + " lineList:"+lineList.size());
		
		setMeasuredDimension(width, height);
	}

	/**
	 * 摆放子view在自己当中的位置：
	 * 按照分好的行，依次摆放到指定的位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);//取出每一行
			
			if(i>0){
				paddingTop += lineList.get(i-1).getHeight() + verticalSpacing;
			}
			
			ArrayList<View> viewList = line.getViewList();
			//1.计算出留白的宽度
			int remainWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getWidth();
			//2.算出每个子view平均得到多少
			float perSpace = remainWidth/viewList.size();
			
			for (int j = 0; j < viewList.size(); j++) {
				View chilidView = viewList.get(j);//取出每个子view
				//3.将perSpace分配给每个子view
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (chilidView.getMeasuredWidth()+perSpace), MeasureSpec.EXACTLY);
				chilidView.measure(widthMeasureSpec, 0);
				
				if(j==0){
					//每行的第一个子view，都是靠左边摆放的
					chilidView.layout(paddingLeft, paddingTop, paddingLeft+chilidView.getMeasuredWidth(), paddingTop+chilidView.getMeasuredHeight());
				}else {
					//不是第一个子view
					View preView = viewList.get(j-1);//获取前一个子view
					int left = preView.getRight() + horizontalSpacing;
					chilidView.layout(left, preView.getTop(), left+chilidView.getMeasuredWidth(), preView.getBottom());
				}
			}
		}
	}

	/**
	 * 一行，用来记录本行的所有TextView
	 * @author Administrator
	 *
	 */
	class Line{
		private ArrayList<View> viewList;//记录本行的所有子view
		private int width;//表示所有子view和所有间距的总宽度
		private int height;//行高，其实是行中高度最大的子view的高度
		
		public Line(){
			viewList = new ArrayList<View>();
		}
		
		/**
		 * 添加子view
		 * @param view
		 */
		public void add(View view){
			if(!viewList.contains(view)){
				viewList.add(view);
			}
			
			//动态更新width和height
			if(viewList.size()==1){
				//第一次添加子view的时候会走这里
				width = view.getMeasuredWidth();
			}else {
				//当前的width+view的宽+horizontalSpacing
				width += view.getMeasuredWidth() + horizontalSpacing;
			}
			
			//取所有子view高度最大的一个
			height = Math.max(height, view.getMeasuredHeight());
		}
		
		public ArrayList<View> getViewList(){
			return viewList;
		}
		
		public int getWidth(){
			return width;
		}
		
		public int getHeight(){
			return height;
		}
	}
	
	public int getVerticalSpacing() {
		return verticalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}


	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}
}
