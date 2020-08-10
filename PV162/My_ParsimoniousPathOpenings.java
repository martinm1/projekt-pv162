package PV162;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import ij.ImagePlus;
import ij.process.ImageConverter;

import ij.io.*;

/** This class represents a single pixel. */
class Pixel
{
	int x, y, value;

	Pixel(int x, int y, int value)
	{
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	Pixel(Pixel pix)
	{
		this.x = pix.getX();
		this.y = pix.getY();
		this.value = pix.getValue();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
}

/** This class represents a single path of pixels. */
class Path
{
	ArrayList<Pixel> pixels;

	Path(){
		pixels = new ArrayList<Pixel>();
	}

	Path(Path path){
		pixels = new ArrayList<Pixel>();

		for(int i = 0; i < path.getArrayList().size(); i++) {
			this.addPixel(new Pixel(path.getArrayList().get(i)));
		}
	}

	Path(LargeValuePath path, ImageProcessor ip){
		pixels = new ArrayList<Pixel>();
		for(int i = 0; i < path.getArrayList().size(); i++){
			LargeValuePixel pixel = path.getArrayList().get(i);
			addPixel(new Pixel(pixel.getX(), pixel.getY(), ip.get(pixel.getX(), pixel.getY())));
		}
	}

	ArrayList<Pixel> getArrayList(){
		return pixels;
	}

	void addPixel(Pixel pixel){
		pixels.add(pixel);
	}
}

/** This class represents a list of pixels. */
class PixelList
{
	Stack<Pixel> pixels;

	PixelList(){
		pixels = new Stack<Pixel>();
	}
	
	void addPixel(Pixel pixel){
		pixels.push(pixel);
	}
	
	/** Find pixel with maximal intensity. */ 
	Pixel findMaxPixel(){
		if(pixels.empty()) return null;
		Pixel largest = pixels.pop();
		for(Pixel pixel : pixels){
			if(pixel.getValue() > largest.getValue()) largest = pixel;
		}
		return largest;
	}
	
	/** Find pixel with minimal intensity. */ 
	Pixel findMinPixel(){
		if(pixels.empty()) return null;
		Pixel smallest = pixels.pop();
		for(Pixel pixel : pixels){
			if(pixel.getValue() < smallest.getValue()) smallest = pixel;
		}
		return smallest;
	}
}

/** This class represents a list of paths. */
class PathList
{
	Stack<Path> paths;

	PathList(){
		paths = new Stack<Path>();
	}
	
	void addPath(Path path){
		paths.push(path);
	}

	void addPath(LargeValuePath path, ImageProcessor ip){
		paths.push(new Path(path, ip));
	}

	Stack<Path> getStack(){
		return paths;
	}
}

/** This class represents a single pixel with high intensity. */
class LargeValuePixel
{
	int x, y;
	long value;
	
	LargeValuePixel(int x, int y, long value)
	{
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	public long getValue(){
		return value;
	}
	
	public void setValue(long value){
		this.value = value;
	}
}

/** This class represents a list of pixels with high intensity. */
class LargeValuePixelList
{
	Stack<LargeValuePixel> pixels;

	LargeValuePixelList(){
		pixels = new Stack<LargeValuePixel>();
	}
	
	void addPixel(LargeValuePixel pixel){
		pixels.push(pixel);
	}

	/** Find pixel with maximal intensity. */ 
	LargeValuePixel findMaxPixel(){
		if(pixels.empty()) return null;
		LargeValuePixel largest = pixels.pop();
		for(LargeValuePixel pixel : pixels){
			if(pixel.getValue() > largest.getValue()) largest = pixel;
		}
		return largest;
	}
}

/** This class represents a single path of pixels with high intensity. */
class LargeValuePath
{
	ArrayList<LargeValuePixel> pixels;

	LargeValuePath()
	{
		pixels = new ArrayList<LargeValuePixel>();
	}

	LargeValuePath(LargeValuePath path){
		pixels = new ArrayList<LargeValuePixel>();
		for(int i =0; i < path.getArrayList().size(); i++) {
			LargeValuePixel pix = path.getArrayList().get(i);
			this.addPixel(new LargeValuePixel(pix.getX(), pix.getY(), pix.getValue()));
		}
	}

	ArrayList<LargeValuePixel> getArrayList(){
		return pixels;
	}

	void addPixel(LargeValuePixel pixel){
		pixels.add(pixel);
	}
	
}

/** This class represents an image of pixels with high intensity. */
class LargeValueImage{
	long[][] image;
       	int w;
        	int h;

	LargeValueImage(int w, int h){
		this.image = new long[w][h];
                	this.w = w;
                	this.h = h;
	}

	LargeValueImage(ImageProcessor ip){
		this.w = ip.getWidth();
		this.h = ip.getHeight();	
		this.image = new long[w][h];
		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h; ++y)
			{
				image[x][y] = ip.get(x,y);
			}
		}
	}
	
	LargeValueImage(LargeValueImage original){
		this.w = original.w;
		this.h = original.h;
		this.image = new long[w][h];
		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h; ++y)
			{
				image[x][y] = original.get(x,y);
			}
		}
	}
	
	/**Replace part of the image beginning in (offsetX, offsetY) by another image. */ 
	void replacePart(int offsetX, int offsetY, LargeValueImage lvip){
		for (int x = 0; x < lvip.getWidth(); ++x)
		{
			for (int y = 0; y < lvip.getHeight(); ++y)
			{
				image[x + offsetX][y + offsetY] = lvip.get(x, y);
			}
		}
		
	}
	
	long get(int x, int y){
		return image[x][y];
	}

	void set(int x, int y, long value){
		image[x][y] = value;
	}
        
        int getWidth(){
		return w;
	}
        
        int getHeight(){
		return h;
	}
}

/** This class represents a path-finder for locally maximal paths. */
class PathFinderLMP
{
	/**Decides whether coordinates (x, y) lie within the image. */ 
	public boolean isInImage(ImageProcessor ip, int x, int y)
	{
		int w = ip.getWidth();
		int h = ip.getHeight();
		if(x >= 0 && x < w && y >= 0 && y < h) return true;
		return false;
	}
	/**Searches for locally maximal paths in SN direction. */ 
	public PathList getMaxPathSN(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int x  = 0; x < w; x++){
			maxPathList.addPath(getPathSN(ip, new Pixel(x, h-1, ip.get(x, h-1))));
		}
		return maxPathList;
	}
	/**Searches for locally maximal path in SN direction beginning in startSN. */ 
	public Path getPathSN(ImageProcessor ip, Pixel startSN){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		Pixel pixel;
		Path pathSN = new Path();

		pixel = startSN;
		
		while(pixel != null){
			pathSN.addPixel(pixel);
			pixel = this.getNextPixelSN(ip, pixel);
		}
		
		return pathSN;
	}
	/**Searches for next pixel in SN direction. */ 
	public Pixel getNextPixelSN(ImageProcessor ip, Pixel thisSN){
		PixelList pl = new PixelList();
		int x = thisSN.getX();
		int y = thisSN.getY();
		if(isInImage(ip, x+1, y-1)) pl.addPixel(new Pixel(x+1, y-1, ip.get(x+1,y-1)));
		if(isInImage(ip, x-1, y-1)) pl.addPixel(new Pixel(x-1, y-1, ip.get(x-1,y-1)));
		if(isInImage(ip, x, y-1)) pl.addPixel(new Pixel(x, y-1, ip.get(x,y-1)));

		return pl.findMaxPixel();
	}
	/**Searches for locally maximal paths in SWNE direction. */ 
	public PathList getMaxPathSWNE(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int x  = 0; x < w; x++){
			maxPathList.addPath(getPathSWNE(ip, new Pixel(x, h-1, ip.get(x, h-1))));
		}
		for(int y  = 0; y < h; y++){
			maxPathList.addPath(getPathSWNE(ip, new Pixel(0, y, ip.get(0, y))));
		}

		return maxPathList;
	}
	/**Searches for locally maximal path in SWNE direction beginning in startSWNE. */ 
	public Path getPathSWNE(ImageProcessor ip, Pixel startSWNE){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		Pixel pixel;
		Path pathSWNE = new Path();

		pixel = startSWNE;
		
		while(pixel != null){
			pathSWNE.addPixel(pixel);
			pixel = this.getNextPixelSWNE(ip, pixel);
		}
		
		return pathSWNE;
	}
	/**Searches for next pixel in SWNE direction. */ 
	public Pixel getNextPixelSWNE(ImageProcessor ip, Pixel thisSWNE){

		PixelList pl = new PixelList();
		int x = thisSWNE.getX();
		int y = thisSWNE.getY();
		if(isInImage(ip, x+1, y)) pl.addPixel(new Pixel(x+1, y, ip.get(x+1, y)));
		if(isInImage(ip, x, y-1)) pl.addPixel(new Pixel(x, y-1, ip.get(x, y-1)));
		if(isInImage(ip, x+1, y-1)) pl.addPixel(new Pixel(x+1, y-1, ip.get(x+1, y-1)));

		return pl.findMaxPixel();
	}

	/**Searches for locally maximal paths in WE direction. */ 
	public PathList getMaxPathWE(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int y  = 0; y < h; y++){
			maxPathList.addPath(getPathWE(ip, new Pixel(0, y, ip.get(0, y))));
		}
		
		return maxPathList;
	}
	
	/**Searches for locally maximal path in WE direction beginning in startWE. */ 
	public Path getPathWE(ImageProcessor ip, Pixel startWE){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		Pixel pixel;
		Path pathWE = new Path();

		pixel = startWE;
		
		while(pixel != null){
			pathWE.addPixel(pixel);
			pixel = this.getNextPixelWE(ip, pixel);
		}
		
		return pathWE;
	}
	
	/**Searches for next pixel in WE direction. */ 
	public Pixel getNextPixelWE(ImageProcessor ip, Pixel thisWE){
		PixelList pl = new PixelList();
		int x = thisWE.getX();
		int y = thisWE.getY();
		if(isInImage(ip, x+1, y-1)) pl.addPixel(new Pixel(x+1, y-1, ip.get(x+1, y-1)));		
		if(isInImage(ip, x+1, y+1)) pl.addPixel(new Pixel(x+1, y+1, ip.get(x+1, y+1)));
		if(isInImage(ip, x+1, y)) pl.addPixel(new Pixel(x+1, y, ip.get(x+1, y)));

		return pl.findMaxPixel();
	}

	/**Searches for locally maximal paths in NWSE direction. */ 
	public PathList getMaxPathNWSE(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int x  = 0; x < w; x++){
			maxPathList.addPath(getPathNWSE(ip, new Pixel(x, 0, ip.get(x, 0))));
		}
		for(int y  = 0; y < h; y++){
			maxPathList.addPath(getPathNWSE(ip, new Pixel(0, y, ip.get(0, y))));
		}

		return maxPathList;
	}
	
	/**Searches for locally maximal path in NWSE direction beginning in startNWSE. */ 
	public Path getPathNWSE(ImageProcessor ip, Pixel startNWSE){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		Pixel pixel;
		Path pathNWSE = new Path();

		pixel = startNWSE;
		
		while(pixel != null){
			pathNWSE.addPixel(pixel);
			pixel = this.getNextPixelNWSE(ip, pixel);
		}
		
		return pathNWSE;
	}
	
	/**Searches for next pixel in NWSE direction. */ 
	public Pixel getNextPixelNWSE(ImageProcessor ip, Pixel thisNWSE){
		PixelList pl = new PixelList();
		int x = thisNWSE.getX();
		int y = thisNWSE.getY();
		if(isInImage(ip, x+1, y)) pl.addPixel(new Pixel(x+1, y, ip.get(x+1, y)));		
		if(isInImage(ip, x, y+1)) pl.addPixel(new Pixel(x, y+1, ip.get(x, y+1)));
		if(isInImage(ip, x+1, y+1)) pl.addPixel(new Pixel(x+1, y+1, ip.get(x+1, y+1)));

		return pl.findMaxPixel();
	}
}




/** This class represents a path-finder for globally maximal paths and beta-maximal paths. */
class PathFinderGMP
{

	/**Cuts a new w x h image beginning at (offsetX, offsetY) from image ip. */ 
	public ImageProcessor getImagePart(int w, int h, int offsetX, int offsetY, ImageProcessor ip){
		ImageProcessor out = new ShortProcessor(w, h);
		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h; ++y)
			{
				out.set(x, y, ip.get(x + offsetX,y + offsetY));
			}
		}
		return out;
	}
	
	/**Decides whether coordinates (x, y) lie within the image. */ 
	public boolean isInImage(LargeValueImage ip, int x, int y)
	{
		int w = ip.getWidth();
		int h = ip.getHeight();
		if(x >= 0 && x < w && y >= 0 && y < h) return true;
		return false;
	}
	
	/**Searches for globally maximal paths in SN direction. */ 
	public PathList getGMPMaxPathSN(ImageProcessor ip){

		LargeValueImage lip = getLambdaSN(ip);
		return getMaxPathSN(lip, ip);
	}

	/**Searches for beta-maximal paths in SN direction. */ 
	public PathList getBMPMaxPathSN(ImageProcessor ip, int height){
		LargeValueImage lip = new LargeValueImage(ip.getWidth(), ip.getHeight());
		ImageProcessor ipPart;
		LargeValueImage lvipPart;

		for(int y = 0; y < ip.getHeight(); y += height){
			if(y + height < ip.getHeight()){
				ipPart = getImagePart(ip.getWidth(), height, 0, y, ip);
				lvipPart = getLambdaSN(ipPart);
				lip.replacePart(0, y, lvipPart);
			}
			else{
				ipPart = getImagePart(ip.getWidth(), ip.getHeight() - y, 0, y, ip);
				lvipPart = getLambdaSN(ipPart);
				lip.replacePart(0, y, lvipPart);
			}
			
		}
		
		return getMaxPathSN(lip, ip);
	}
	
	/**Searches for beta-maximal paths in WE direction. */ 
	public PathList getBMPMaxPathWE(ImageProcessor ip, int width){
		LargeValueImage lip = new LargeValueImage(ip.getWidth(), ip.getHeight());
		ImageProcessor ipPart;
		LargeValueImage lvipPart;

		for(int x = 0; x < ip.getWidth(); x += width){
			if(x + width < ip.getWidth()){
				ipPart = getImagePart(width, ip.getHeight(), x, 0, ip);
				lvipPart = getLambdaWE(ipPart);
				lip.replacePart(x, 0, lvipPart);
			}
			else{
				ipPart = getImagePart(ip.getWidth() - x, ip.getHeight(), x, 0, ip);
				lvipPart = getLambdaWE(ipPart);
				lip.replacePart(x, 0, lvipPart);
			}
			
		}
		
		return getMaxPathWE(lip, ip);
	}

	/**Computes lambda image for SN direction. */ 
	public LargeValueImage getLambdaSN(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		long value;

		LargeValueImage lip = new LargeValueImage(ip);
		
		LargeValueImage tip = new LargeValueImage(lip);
		
		
		for (int y = 1; y < h; ++y)
		{
			for (int x = 0; x < w; ++x)
			{
				value = getNextPixelSN(lip, new LargeValuePixel(x, y, 0)).getValue();
				lip.set(x,y, lip.get(x,y) + value);
			}
		}
		for (int y = h-2; y >=0 ; --y)
		{
			for (int x = 0; x < w; ++x)
			{
				value = getPreviousPixelSN(tip, new LargeValuePixel(x, y, 0)).getValue();
				tip.set(x,y, tip.get(x,y) + value);
			}
		}
		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h ; ++y)
			{
				lip.set(x,y, tip.get(x,y) + lip.get(x,y));
			}
		}
		return lip;
	}
	
	/**Searches for globally maximal paths in WE direction. */ 
	public PathList getGMPMaxPathWE(ImageProcessor ip){

		LargeValueImage lip = getLambdaWE(ip);
		return getMaxPathWE(lip, ip);
	}

	/**Computes lambda image for WE direction. */ 
	public LargeValueImage getLambdaWE(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		long value;

		LargeValueImage lip = new LargeValueImage(ip);
		
		LargeValueImage tip = new LargeValueImage(lip);

		for (int x = w-2; x >= 0; --x)
		{
			for (int y = 0; y < h; ++y)
			{
				value = getNextPixelWE(lip, new LargeValuePixel(x, y, 0)).getValue();
				lip.set(x,y, lip.get(x,y) + value);
			}
		}
		for (int x = 1; x < w-1; ++x)
		{
			for (int y = 0; y < h; ++y)
			{
				value = getPreviousPixelWE(tip, new LargeValuePixel(x, y, 0)).getValue();
				tip.set(x,y, tip.get(x,y) + value);
			}
		}
		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h ; ++y)
			{
				lip.set(x,y, tip.get(x,y) + lip.get(x,y));
			}
		}
		return lip;
	}
	
	/**Searches for globally maximal paths in SWNE direction. */ 
	public PathList getGMPMaxPathSWNE(ImageProcessor ip){

		LargeValueImage lip = getLambdaSWNE(ip);
		return getMaxPathSWNE(lip, ip);
	}

	/**Computes lambda image for SWNE direction. */ 
	public LargeValueImage getLambdaSWNE(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		long value;

		LargeValueImage lip = new LargeValueImage(ip);
		
		LargeValueImage tip = new LargeValueImage(lip);
		
		int i = 1;
		int x1 =1;
		int y1 = 1;
		while(i < w-1 && i < h-1)
		{
			x1= w-1-i;
			for(y1 = 0; y1 < i; y1++)
			{
				value = getNextPixelSWNE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
				lip.set(x1,y1, lip.get(x1,y1) + value);
			}
			y1 = i;
			for(x1 = w-1; x1 > w-1-i; x1--)
			{
				value = getNextPixelSWNE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
				lip.set(x1,y1, lip.get(x1,y1) + value);
			}
			x1= w-1-i;
			y1 = i;
			
			value = getNextPixelSWNE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
			lip.set(x1,y1, lip.get(x1,y1) + value);
			
			i++;
		}
		if(i  == w-1){
			while(i < h-1){
				y1 = i;
				for(x1 = w-1; x1 >= 0 ; x1--){
					value = getNextPixelSWNE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
					lip.set(x1,y1, lip.get(x1,y1) + value);
				}
				i++;
			}
		}
		else if(i  == h-1){
			while(i < w-1){
				x1= w-1-i;
				for(y1 = 0; y1 <= h-1; y1++){
					value = getNextPixelSWNE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
					lip.set(x1,y1, lip.get(x1,y1) + value);
				}
				i++;
			}
		}

		i = 1;
		while(i < w-1 && i < h-1)
		{
			x1= i;
			for(y1 = h-1; y1 > h-1-i; y1--)
			{
				value = getPreviousPixelSWNE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
				tip.set(x1,y1, tip.get(x1,y1) + value);
			}
			y1 = h-1-i;
			for(x1 = 0; x1 < i; x1++)
			{
				value = getPreviousPixelSWNE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
				tip.set(x1,y1, tip.get(x1,y1) + value);
			}
			x1= i;
			y1 = h-1-i;
			
			value = getPreviousPixelSWNE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
			tip.set(x1,y1, tip.get(x1,y1) + value);
			
			i++;
		}
		if(i  == w-1){
			while(i < h-1){
				y1 = h-1-i;
				for(x1 = 0; x1 <= w-1 ; x1++){
					value = getPreviousPixelSWNE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
					tip.set(x1,y1, tip.get(x1,y1) + value);
				}
				i++;
			}
		}
		else if(i  == h-1){
			while(i < w-1){
				x1= i;
				for(y1 = h-1; y1 >= 0; y1--){
					value = getPreviousPixelSWNE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
					tip.set(x1,y1, tip.get(x1,y1) + value);
				}
				i++;
			}
		}

		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h ; ++y)
			{
				lip.set(x,y, tip.get(x,y) + lip.get(x,y));
			}
		}
		return lip;
	}
	
	/**Searches for globally maximal paths in SWNE direction. */ 
	public PathList getGMPMaxPathNWSE(ImageProcessor ip){

		LargeValueImage lip = getLambdaNWSE(ip);
		return getMaxPathNWSE(lip, ip);
	}

	/**Computes lambda image for NWSE direction. */ 
	public LargeValueImage getLambdaNWSE(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		long value;

		LargeValueImage lip = new LargeValueImage(ip);
		
		LargeValueImage tip = new LargeValueImage(lip);
		
		int i = 1;
		int x1 =1;
		int y1 = 1;
		while(i < w-1 && i < h-1)
		{
			x1= w-1-i;
			for(y1 = h-1; y1 > h-1-i; y1--)
			{
				value = getNextPixelNWSE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
				lip.set(x1,y1, lip.get(x1,y1) + value);
			}
			y1 = h-1-i;
			for(x1 = w-1; x1 > w-1-i; x1--)
			{
				value = getNextPixelNWSE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
				lip.set(x1,y1, lip.get(x1,y1) + value);
			}
			x1= w-1-i;
			y1 = h-1-i;
			
			value = getNextPixelNWSE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
			lip.set(x1,y1, lip.get(x1,y1) + value);
			
			i++;
		}
		if(i  == w-1){
			while(i < h-1){
				y1 = h-1-i;
				for(x1 = w-1; x1 >= 0 ; x1--){
					value = getNextPixelNWSE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
					lip.set(x1,y1, lip.get(x1,y1) + value);
				}
				i++;
			}
		}
		else if(i  == h-1){
			while(i < w-1){
				x1= w-1-i;
				for(y1 = h-1; y1 >= 0; y1--){
					value = getNextPixelNWSE(lip, new LargeValuePixel(x1, y1, 0)).getValue();
					lip.set(x1,y1, lip.get(x1,y1) + value);
				}
				i++;
			}
		}
		i = 1;
		while(i < w-1 && i < h-1)
		{
			x1= i;
			for(y1 = 0; y1 < i; y1++)
			{
				value = getPreviousPixelNWSE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
				tip.set(x1,y1, tip.get(x1,y1) + value);
			}
			y1 = i;
			for(x1 = 0; x1 < i; x1++)
			{
				value = getPreviousPixelNWSE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
				tip.set(x1,y1, tip.get(x1,y1) + value);
			}
			x1 = i;
			y1 = i;
			
			value = getPreviousPixelNWSE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
			tip.set(x1,y1, tip.get(x1,y1) + value);
			
			i++;
		}
		if(i  == w-1){
			while(i < h-1){
				y1 = i;
				for(x1 = 0; x1 <= w-1 ; x1++){
					value = getPreviousPixelNWSE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
					tip.set(x1,y1, tip.get(x1,y1) + value);
				}
				i++;
			}
		}
		else if(i  == h-1){
			while(i < w-1){
				x1= i;
				for(y1 = 0; y1 <= h-1; y1++){
					value = getPreviousPixelNWSE(tip, new LargeValuePixel(x1, y1, 0)).getValue();
					tip.set(x1,y1, tip.get(x1,y1) + value);
				}
				i++;
			}
		}
		for (int x = 0; x < w; ++x)
		{
			for (int y = 0; y < h ; ++y)
			{
				lip.set(x,y, tip.get(x,y) + lip.get(x,y));
			}
		}
		return lip;
	}
	
	/**Searches for locally maximal paths in SN direction in large value image. */ 
	public PathList getMaxPathSN(LargeValueImage ip, ImageProcessor mip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int x  = 0; x < w; x++){
			maxPathList.addPath(getPathSN(ip, new LargeValuePixel(x, h-1, ip.get(x, h-1))), mip);
		}
		
		return maxPathList;
	}
	
	/**Searches for locally maximal path in SN direction beginning in startSN. */ 
	public LargeValuePath getPathSN(LargeValueImage ip, LargeValuePixel startSN){ 
		int w = ip.getWidth();
		int h = ip.getHeight();	
		LargeValuePixel pixel;
		LargeValuePath pathSN = new LargeValuePath();

		pixel = startSN;
		
		while(pixel != null){
			pathSN.addPixel(pixel);
			pixel = this.getNextPixelSN(ip, pixel);
		}
		
		return pathSN;
	}
	
	/**Searches for next pixel in SN direction. */ 
	public LargeValuePixel getNextPixelSN(LargeValueImage ip, LargeValuePixel thisSN){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisSN.getX();
		int y = thisSN.getY();
		if(isInImage(ip, x+1, y-1)) pl.addPixel(new LargeValuePixel(x+1, y-1, ip.get(x+1,y-1)));
		if(isInImage(ip, x-1, y-1)) pl.addPixel(new LargeValuePixel(x-1, y-1, ip.get(x-1,y-1)));
		if(isInImage(ip, x, y-1)) pl.addPixel(new LargeValuePixel(x, y-1, ip.get(x,y-1)));

		return pl.findMaxPixel();
	}
	
	/**Searches for previous pixel in SN direction. */ 
	public LargeValuePixel getPreviousPixelSN(LargeValueImage ip, LargeValuePixel thisSN){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisSN.getX();
		int y = thisSN.getY();
		if(isInImage(ip, x+1, y+1)) pl.addPixel(new LargeValuePixel(x+1, y+1, ip.get(x+1,y+1)));
		if(isInImage(ip, x-1, y+1)) pl.addPixel(new LargeValuePixel(x-1, y+1, ip.get(x-1,y+1)));
		if(isInImage(ip, x, y+1)) pl.addPixel(new LargeValuePixel(x, y+1, ip.get(x,y+1)));

		return pl.findMaxPixel();
	}
	
	/**Searches for locally maximal paths in SWNE direction in large value image. */ 
	public PathList getMaxPathSWNE(LargeValueImage ip, ImageProcessor mip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int x  = 0; x < w; x++){
			maxPathList.addPath(getPathSWNE(ip, new LargeValuePixel(x, h-1, ip.get(x, h-1))), mip);
		}
		for(int y  = 0; y < h; y++){
			maxPathList.addPath(getPathSWNE(ip, new LargeValuePixel(0, y, ip.get(0, y))), mip);
		}

		return maxPathList;
	}
	
	/**Searches for locally maximal path in SWNE direction beginning in startSWNE. */ 
	public LargeValuePath getPathSWNE(LargeValueImage ip, LargeValuePixel startSWNE){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		LargeValuePixel pixel;
		LargeValuePath pathSWNE = new LargeValuePath();

		pixel = startSWNE;
		
		while(pixel != null){
			pathSWNE.addPixel(pixel);
			pixel = this.getNextPixelSWNE(ip, pixel);
		}
		
		return pathSWNE;
	}

	/**Searches for next pixel in SWNE direction. */ 
	public LargeValuePixel getNextPixelSWNE(LargeValueImage ip, LargeValuePixel thisSWNE){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisSWNE.getX();
		int y = thisSWNE.getY();
		if(isInImage(ip, x+1, y)) pl.addPixel(new LargeValuePixel(x+1, y, ip.get(x+1, y)));
		if(isInImage(ip, x, y-1)) pl.addPixel(new LargeValuePixel(x, y-1, ip.get(x, y-1)));
		if(isInImage(ip, x+1, y-1)) pl.addPixel(new LargeValuePixel(x+1, y-1, ip.get(x+1, y-1)));

		return pl.findMaxPixel();
	}

	/**Searches for previous pixel in SWNE direction. */ 
	public LargeValuePixel getPreviousPixelSWNE(LargeValueImage ip, LargeValuePixel thisSWNE){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisSWNE.getX();
		int y = thisSWNE.getY();
		if(isInImage(ip, x, y+1)) pl.addPixel(new LargeValuePixel(x, y+1, ip.get(x, y+1)));
		if(isInImage(ip, x-1, y)) pl.addPixel(new LargeValuePixel(x-1, y, ip.get(x-1, y)));
		if(isInImage(ip, x-1, y+1)) pl.addPixel(new LargeValuePixel(x-1, y+1, ip.get(x-1, y+1)));

		return pl.findMaxPixel();
	}
	
	/**Searches for locally maximal paths in WE direction in large value image. */ 
	public PathList getMaxPathWE(LargeValueImage ip, ImageProcessor mip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int y  = 0; y < h; y++){
			maxPathList.addPath(getPathWE(ip, new LargeValuePixel(0, y, ip.get(0, y))), mip);
		}
		
		return maxPathList;
	}
	
	/**Searches for locally maximal paths in WE direction. */ 
	public LargeValuePath getPathWE(LargeValueImage ip, LargeValuePixel startWE){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		LargeValuePixel pixel;
		LargeValuePath pathWE = new LargeValuePath();

		pixel = startWE;
		
		while(pixel != null){
			pathWE.addPixel(pixel);
			pixel = this.getNextPixelWE(ip, pixel);
		}
		
		return pathWE;
	}
	
	/**Searches for next pixel in WE direction. */ 
	public LargeValuePixel getNextPixelWE(LargeValueImage ip, LargeValuePixel thisWE){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisWE.getX();
		int y = thisWE.getY();
		if(isInImage(ip, x+1, y-1)) pl.addPixel(new LargeValuePixel(x+1, y-1, ip.get(x+1, y-1)));		
		if(isInImage(ip, x+1, y+1)) pl.addPixel(new LargeValuePixel(x+1, y+1, ip.get(x+1, y+1)));
		if(isInImage(ip, x+1, y)) pl.addPixel(new LargeValuePixel(x+1, y, ip.get(x+1, y)));

		return pl.findMaxPixel();
	}
	
	/**Searches for previous pixel in WE direction. */ 
	public LargeValuePixel getPreviousPixelWE(LargeValueImage ip, LargeValuePixel thisWE){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisWE.getX();
		int y = thisWE.getY();
		if(isInImage(ip, x-1, y-1)) pl.addPixel(new LargeValuePixel(x-1, y-1, ip.get(x-1, y-1)));		
		if(isInImage(ip, x-1, y+1)) pl.addPixel(new LargeValuePixel(x-1, y+1, ip.get(x-1, y+1)));
		if(isInImage(ip, x-1, y)) pl.addPixel(new LargeValuePixel(x-1, y, ip.get(x-1, y)));

		return pl.findMaxPixel();
	}
	
	/**Searches for locally maximal paths in NWSE direction in large value image. */ 
	public PathList getMaxPathNWSE(LargeValueImage ip, ImageProcessor mip){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		PathList maxPathList = new PathList();
		
		for(int x  = 0; x < w; x++){
			maxPathList.addPath(getPathNWSE(ip, new LargeValuePixel(x, 0, ip.get(x, 0))), mip);
		}
		for(int y  = 0; y < h; y++){
			maxPathList.addPath(getPathNWSE(ip, new LargeValuePixel(0, y, ip.get(0, y))), mip);
		}

		return maxPathList;
	}

	/**Searches for locally maximal path in NWSE direction beginning in startNWSE. */ 
	public LargeValuePath getPathNWSE(LargeValueImage ip, LargeValuePixel startNWSE){
		int w = ip.getWidth();
		int h = ip.getHeight();	
		LargeValuePixel pixel;
		LargeValuePath pathNWSE = new LargeValuePath();

		pixel = startNWSE;
		
		while(pixel != null){
			pathNWSE.addPixel(pixel);
			pixel = this.getNextPixelNWSE(ip, pixel);
		}
		
		return pathNWSE;
	}

	/**Searches for next pixel in NWSE direction. */ 
	public LargeValuePixel getNextPixelNWSE(LargeValueImage ip, LargeValuePixel thisNWSE){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisNWSE.getX();
		int y = thisNWSE.getY();
		if(isInImage(ip, x+1, y)) pl.addPixel(new LargeValuePixel(x+1, y, ip.get(x+1, y)));		
		if(isInImage(ip, x, y+1)) pl.addPixel(new LargeValuePixel(x, y+1, ip.get(x, y+1)));
		if(isInImage(ip, x+1, y+1)) pl.addPixel(new LargeValuePixel(x+1, y+1, ip.get(x+1, y+1)));

		return pl.findMaxPixel();
	}
	
	/**Searches for previous pixel in NWSE direction. */ 
	public LargeValuePixel getPreviousPixelNWSE(LargeValueImage ip, LargeValuePixel thisNWSE){
		LargeValuePixelList pl = new LargeValuePixelList();
		int x = thisNWSE.getX();
		int y = thisNWSE.getY();
		if(isInImage(ip, x-1, y)) pl.addPixel(new LargeValuePixel(x-1, y, ip.get(x-1, y)));		
		if(isInImage(ip, x, y-1)) pl.addPixel(new LargeValuePixel(x, y-1, ip.get(x, y-1)));
		if(isInImage(ip, x-1, y-1)) pl.addPixel(new LargeValuePixel(x-1, y-1, ip.get(x-1, y-1)));

		return pl.findMaxPixel();
	}
}

/** This plugin searches for long uneven structures. */
public class My_ParsimoniousPathOpenings implements PlugInFilter 
{
	/**Creates a grayscale image from the original. */ 
	public ImageProcessor getGrayScaleIP(ImageProcessor ip){
		ImagePlus img = new ImagePlus("name", ip);
		ImageConverter ic = new ImageConverter(img);
		ic.convertToGray8();
		return img.getProcessor(); 
	}
		
	/**Creates an inverse image from the original. */ 
	public ImageProcessor getInverseIP(ImageProcessor ip){
		ImageProcessor out = ip.duplicate();
		out.invert();
		return out; 
	}

	/**Shows the image. */ 
	public void showIP(ImageProcessor ip){
		ImagePlus img = new ImagePlus("image", ip);
		img.show();
	}
	
	/**Adds a path to the image. */ 
	public ImageProcessor fullPathDilationAdd(ImageProcessor ip, Path path){
		ImageProcessor out = ip.duplicate();
		
		for(Pixel pixel : path.getArrayList()){
			out.putPixel(pixel.getX(), pixel.getY(), 200);
		}

		return out;
	}

	/**Adds a path list to the image. */
	public ImageProcessor fullPathDilationAdd(ImageProcessor ip, PathList  pl){
		ImageProcessor out = ip.duplicate();
		
		for(Path path : pl.getStack()){
			out = fullPathDilationAdd(out, path);
		}

		return out;
	}
	
	/**Shows a single path from the image. */
	public ImageProcessor fullPathDilation(ImageProcessor ip, Path path){
		ImageProcessor out = new ShortProcessor(ip.getWidth(), ip.getHeight());
		
		for(Pixel pixel : path.getArrayList()){
			out.putPixel(pixel.getX(), pixel.getY(), 200);
		}

		return out;
	}
	
	/**Shows all paths from the image. */
	public ImageProcessor fullPathDilation(ImageProcessor ip, PathList pl){
		ImageProcessor out = new ShortProcessor(ip.getWidth(), ip.getHeight());
		
		for(Path path : pl.getStack()){
			out = fullPathDilationAdd(out, path);
		}

		return out;
	}
	

	/**Computes path opening on all given paths. */
	public ImageProcessor parsimoniousPathOpening(ImageProcessor ip, PathList pl, int openingSize){
		ImageProcessor out = ip.duplicate();
		out.threshold(256);
		for(Path path : pl.getStack()){
			out = drawPath(out, parsimoniousPathOpening(path, openingSize));
		}
		return out;
	}
	
	/**Draws a single path. */
	public ImageProcessor drawPath(ImageProcessor ip, Path path){
		ImageProcessor out = ip.duplicate();
		for(Pixel pixel : path.getArrayList()){
			out.putPixel(pixel.getX(), pixel.getY(), Math.max(out.getPixel(pixel.getX(), pixel.getY()), pixel.getValue()));
		}
		return out;
	}

	/**Computes path opening on a single path. */
	public Path parsimoniousPathOpening(Path path, int openingSize){
		Path out = new Path();
		out = erosion(path, (int) (openingSize/2) );
		out = dilation(out, (int) (openingSize/2) );
		return out;

	}
	
	/**Computes erosion on a path. */
	public Path erosion(Path path, int elementSize){
		PixelList pl;
		Path myPath = new Path();
		
		for(int i = 0; i < path.getArrayList().size(); i++){
			pl = new PixelList();
			for(int j = -elementSize; j <= elementSize; j++){
				if(i+j < 0 || i+j > path.getArrayList().size()) pl.addPixel(new Pixel(0,0,0));
				if(i+j >= 0 &&  i+j < path.getArrayList().size()) pl.addPixel(path.getArrayList().get(i+j));
			}
			myPath.addPixel(new Pixel(path.getArrayList().get(i).getX(), path.getArrayList().get(i).getY(), pl.findMinPixel().getValue()));
		}	
		return myPath;
	}
	
	/**Computes dilation on a path. */
	public Path dilation(Path path, int elementSize){
		PixelList pl;
		Path myPath = new Path();
		
		for(int i = 0; i < path.getArrayList().size(); i++){
			pl = new PixelList();
			for(int j = -elementSize; j <= elementSize; j++){
				if(i+j >= 0 &&  i+j < path.getArrayList().size()) pl.addPixel(path.getArrayList().get(i+j));
			}
			myPath.addPixel(new Pixel(path.getArrayList().get(i).getX(), path.getArrayList().get(i).getY(), pl.findMaxPixel().getValue()));
		}
		return myPath;
	}

	/**Blackens all pixels with intensity less than cutOff. */
	public ImageProcessor getBlackBackgroundIP(ImageProcessor ip, int cutOff){
		ImageProcessor out = ip.duplicate();
		int w = ip.getWidth();
		int h = ip.getHeight();

		for(int i = 0; i < w; i++)
			for(int j = 0; j < h; j++)
				if(out.getPixel(i,j) < cutOff) out.putPixel(i, j, 0);
		return out;
	}
	
	/**Computes image created by path opening on GMP paths. */
	public ImageProcessor getPathOpeningsGMP(ImageProcessor ip, int openingSize, boolean SN, boolean SWNE, boolean WE, boolean NWSE){
		ImageProcessor lip;
		PathFinderGMP gmp = new PathFinderGMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = gmp.getGMPMaxPathSN(ip);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(SWNE) {
			PathList myPaths2 = gmp.getGMPMaxPathSWNE(ip);
			for(Path path : myPaths2.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 = gmp.getGMPMaxPathWE(ip);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(NWSE) {
			PathList myPaths4 = gmp.getGMPMaxPathNWSE(ip);
			for(Path path : myPaths4.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = parsimoniousPathOpening(ip, myPaths, openingSize);
		return lip;
	}

	/**Computes image created by path opening on LMP paths. */
	public ImageProcessor getPathOpeningsLMP(ImageProcessor ip, int openingSize, boolean SN, boolean SWNE, boolean WE, boolean NWSE){
		ImageProcessor lip;
		PathFinderLMP lmp = new PathFinderLMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = lmp.getMaxPathSN(ip);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(SWNE) {
			PathList myPaths2 = lmp.getMaxPathSWNE(ip);
			for(Path path : myPaths2.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 =  lmp.getMaxPathWE(ip);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(NWSE) {
			PathList myPaths4 = lmp.getMaxPathNWSE(ip);
			for(Path path : myPaths4.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = parsimoniousPathOpening(ip, myPaths, openingSize);
		return lip;
	}
	
	/**Computes image created by path opening on beta-MP paths. */
	public ImageProcessor getPathOpeningsBMP(ImageProcessor ip, int openingSize, int beta, boolean SN, boolean WE){
		ImageProcessor lip;
		PathFinderGMP gmp = new PathFinderGMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = gmp.getBMPMaxPathSN(ip, beta);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 =  gmp.getBMPMaxPathWE(ip, beta);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = parsimoniousPathOpening(ip, myPaths, openingSize);
		return lip;
	}
	
	/**Adds GMP paths to the image. */
	public ImageProcessor addPathsGMP(ImageProcessor ip, boolean SN, boolean SWNE, boolean WE, boolean NWSE){
		ImageProcessor lip;
		PathFinderGMP gmp = new PathFinderGMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = gmp.getGMPMaxPathSN(ip);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(SWNE) {
			PathList myPaths2 = gmp.getGMPMaxPathSWNE(ip);
			for(Path path : myPaths2.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 = gmp.getGMPMaxPathWE(ip);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(NWSE) {
			PathList myPaths4 = gmp.getGMPMaxPathNWSE(ip);
			for(Path path : myPaths4.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = fullPathDilationAdd(ip, myPaths);
		return lip;
	}
	
	/**Adds LMP paths to the image. */
	public ImageProcessor addPathsLMP(ImageProcessor ip, boolean SN, boolean SWNE, boolean WE, boolean NWSE){
		ImageProcessor lip;
		PathFinderLMP lmp = new PathFinderLMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = lmp.getMaxPathSN(ip);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(SWNE) {
			PathList myPaths2 = lmp.getMaxPathSWNE(ip);
			for(Path path : myPaths2.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 =  lmp.getMaxPathWE(ip);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(NWSE) {
			PathList myPaths4 = lmp.getMaxPathNWSE(ip);
			for(Path path : myPaths4.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = fullPathDilationAdd(ip, myPaths);
		return lip;
	}
	
	/**Shows GMP paths from the image. */
	public ImageProcessor showPathsGMP(ImageProcessor ip, boolean SN, boolean SWNE, boolean WE, boolean NWSE){
		ImageProcessor lip;
		PathFinderGMP gmp = new PathFinderGMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = gmp.getGMPMaxPathSN(ip);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(SWNE) {
			PathList myPaths2 = gmp.getGMPMaxPathSWNE(ip);
			for(Path path : myPaths2.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 = gmp.getGMPMaxPathWE(ip);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(NWSE) {
			PathList myPaths4 = gmp.getGMPMaxPathNWSE(ip);
			for(Path path : myPaths4.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = fullPathDilation(ip, myPaths);
		return lip;
	}
	
	/**Shows LMP paths from the image. */
	public ImageProcessor showPathsLMP(ImageProcessor ip, boolean SN, boolean SWNE, boolean WE, boolean NWSE){
		ImageProcessor lip;
		PathFinderLMP lmp = new PathFinderLMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = lmp.getMaxPathSN(ip);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(SWNE) {
			PathList myPaths2 = lmp.getMaxPathSWNE(ip);
			for(Path path : myPaths2.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 =  lmp.getMaxPathWE(ip);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(NWSE) {
			PathList myPaths4 = lmp.getMaxPathNWSE(ip);
			for(Path path : myPaths4.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = fullPathDilation(ip, myPaths);
		return lip;
	}

	/**Adds BMP paths to the image. */
	public ImageProcessor addPathsBMP(ImageProcessor ip, int beta, boolean SN, boolean WE){
		ImageProcessor lip;
		PathFinderGMP gmp = new PathFinderGMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = gmp.getBMPMaxPathSN(ip, beta);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 =  gmp.getBMPMaxPathWE(ip, beta);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = fullPathDilationAdd(ip, myPaths);
		return lip;
	}
	
	/**Shows BMP paths from the image. */
	public ImageProcessor showPathsBMP(ImageProcessor ip, int beta, boolean SN, boolean WE){
		ImageProcessor lip;
		PathFinderGMP gmp = new PathFinderGMP();
		
		PathList myPaths = new PathList();
		
		if(SN) {
			PathList myPaths1 = gmp.getBMPMaxPathSN(ip, beta);
			for(Path path : myPaths1.getStack()){
				myPaths.getStack().push(path);
			}
		}
		if(WE) {
			PathList myPaths3 =  gmp.getBMPMaxPathWE(ip, beta);
			for(Path path : myPaths3.getStack()){
				myPaths.getStack().push(path);
			}
		}
		lip = fullPathDilation(ip, myPaths);
		return lip;
	}
	
	public int setup(String arg, ImagePlus im) 
	{
		// this plugin accepts all supported image types
		return DOES_ALL; 
	}

	public void run(ImageProcessor ip) 
	{
		ImageProcessor lip;

		int openingSize = 50; //velikost otevreni
		int beta = 30;	//parametr, ktery se pouzije pri metode BMP
		boolean SN = true; //include SN paths? //zahrnout SN cesty?
		boolean SWNE = true; //include SWNE paths? //zahrnout SWNE cesty?
		boolean WE = true; //include WE paths? //zahrnout WE cesty?
		boolean NWSE = true; //include NWSE paths? //zahrnout NWSE cesty?
		
		boolean inversion = false; //invert the image before starting the module? //invertovat obrazek pred a po spusteni modulu?(pro hledani tmavych struktur)
		boolean blackBG = false; //set intensity of pixels with intensity < cutOff to zero? //vynulovat pixely s hodnotou < cutOff?
		int cutOff = 80;

		ip = getGrayScaleIP(ip); 

		new FileSaver(new ImagePlus("a", ip)).saveAsGif("image1-grayscale.tif");

		if(inversion){
			ip = getInverseIP(ip); 

			new FileSaver(new ImagePlus("a", ip)).saveAsGif("image1.1-inverse.tif");
		}
		if(blackBG){
			ip = getBlackBackgroundIP(ip, cutOff);
		
			new FileSaver(new ImagePlus("a", ip)).saveAsGif("image1.2-blackBG.tif");
		}
		
		//zvoleni operace - pro provedeni odkomentovat prave jeden radek se zvolenou operaci //uncomment the line with the operation you want
		//lip = getPathOpeningsGMP(ip, openingSize, SN, SWNE, WE, NWSE);
		lip = getPathOpeningsLMP(ip, openingSize, SN, SWNE, WE, NWSE);
		//lip = getPathOpeningsBMP(ip, openingSize, beta, SN, WE);
		//lip = addPathsGMP(ip, SN, SWNE, WE, NWSE);
		//lip = addPathsLMP(ip, SN, SWNE, WE, NWSE);
		//lip = showPathsGMP(ip, SN, SWNE, WE, NWSE);
		//lip = showPathsLMP(ip, SN, SWNE, WE, NWSE);
		//lip = addPathsBMP(ip, beta, SN, WE);
		//lip = showPathsBMP(ip, beta, SN, WE);


		new FileSaver(new ImagePlus("a", lip)).saveAsGif("image2-result.tif");

		if(inversion){
			lip = getInverseIP(lip); 
			new FileSaver(new ImagePlus("a", lip)).saveAsGif("image2.1-result.tif");
		}
		showIP(lip);
	}
	
}
