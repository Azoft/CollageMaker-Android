package com.redmadrobot.azoft.collage.utils.collagegenerators;

import com.redmadrobot.azoft.collage.data.Collage;

/**
 * This class helps to create collages from it's number.
 * <p/>
 * Date: 4/8/2014
 * Time: 4:46 PM
 *
 * @author MiG35
 */
public interface CollageFactory {

	Collage getCollage(final int number);

	int getCollageCount();
}