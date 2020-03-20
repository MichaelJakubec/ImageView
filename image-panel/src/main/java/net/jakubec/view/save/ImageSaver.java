/*
 * Copyright 2018 Michael Jakubec
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

package net.jakubec.view.save;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class ImageSaver {
	public static File save(BufferedImage image, String dirPath, String imgName) throws IOException{
		String[] n=imgName.split("\\.");
		if (n[n.length-1].equals("jpg")){
			return writeJPG(image,dirPath, imgName);
		}else{
			File file=new File(dirPath+File.separator+imgName);
			ImageIO.write(image, n[n.length-1], file);
			return file;
		}
		
	}
	
	public static File saveAs(BufferedImage image, String dirPath) throws IOException{
		JFileChooser chooser = new JFileChooser(dirPath);
		SwingUtilities.updateComponentTreeUI(chooser);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		chooser.showSaveDialog(null);
		
		File save=chooser.getSelectedFile();
		System.out.println(save.getName());
		String[] temp=(save.getName()).split("\\.");

		System.out.println(temp.length);
		for (int i=0;i<temp.length;i++){
			System.out.println(temp[i]);
		}
		if (temp[temp.length-1].equals("jpg")){
			return writeJPG(image,dirPath,save.getName());
		}else{
			ImageIO.write(image, temp[(temp.length-1)], save);
			return save;
		}
		
	}
	private static File writeJPG(BufferedImage image, String dirPath, String imgName){
		try{
	
				File file= new File(dirPath+File.separator+imgName);
				ImageWriter writer = ImageIO.getImageWritersByFormatName( "jpg" ).next(); 
			    ImageOutputStream ios = ImageIO.createImageOutputStream( file ); 
			    writer.setOutput( ios ); 
			    ImageWriteParam iwparam = new JPEGImageWriteParam( Locale.getDefault()); 
			    iwparam.setCompressionMode( ImageWriteParam.MODE_EXPLICIT ) ; 
			    float i=1f;
			    iwparam.setCompressionQuality( i); 
			    writer.write( null, new IIOImage(image, null, null), iwparam ); 
			    ios.flush(); 
			    writer.dispose(); 
			    ios.close(); 
			    return file;
			}catch(IOException IOE){
				System.out.println(IOE);
				return null;
			}
	}
}
