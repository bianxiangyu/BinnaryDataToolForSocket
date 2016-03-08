package com.centling.radio.code.xml;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgProperty {
    private final static Logger LOG = LoggerFactory.getLogger(MsgProperty.class);
    public final static Integer HEAD_LENGTH = 32;
    public final static Integer START_TAG_LENGTH = 4;
    public final static Integer SIZE_LENGTH = 4;
    public final static Integer FUNC_ID_LENGTH = 4;
    public final static Integer FROM_ADDRESS_LENGTH = 10;
    public final static Integer TO_ADDRESS_LENGTH = 10;
    public final static Integer REPEAT_PIECE_SIZE = 1;
    public final static Integer CHOSE_PIECE_SIZE = 1;

    private String id;
    private boolean ifChose;
    private String whichbody;
    private int repeatSize;
    private int unNormalRepeatSize;
    private int baseLength;
    private int extendBaseLength = 0;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public boolean isIfChose() {
	return ifChose;
    }

    public void setIfChose(boolean ifChose) {
	this.ifChose = ifChose;
    }

    public String getWhichbody() {
	return whichbody;
    }

    public void setWhichbody(String whichbody) {
	this.whichbody = whichbody;
    }

    public void setRepeatSize(int repeatSize) {
	this.repeatSize = repeatSize;
    }

    public void setUnNormalRepeatSize(int unNormalRepeatSize) {
	this.unNormalRepeatSize = unNormalRepeatSize;
    }

    public int getRepeatSize() {
	return this.repeatSize;
    }

    public int getUnNormalRepeatSize() {
	return this.unNormalRepeatSize;
    }

    public void setBaseLength(int baseLength) {
	this.baseLength = baseLength;
    }

    public void setExtendBaseLength(int extendBaseLength) {
	this.extendBaseLength = extendBaseLength;
    }

    public Integer getLength() {
	if (repeatSize > 0 && unNormalRepeatSize > 0) {
	    LOG.error("信息属性出错,repeatSize:[{}]--unNormalRepeatSize:[{}]", repeatSize, unNormalRepeatSize);
	    return null;
	}
	if (repeatSize > 0) {
	    return baseLength + extendBaseLength * repeatSize;
	} else {
	    return baseLength + extendBaseLength * unNormalRepeatSize;
	}

    }

   

    @Override
    public boolean equals(Object obj) {
	if (obj != null && MsgProperty.class == obj.getClass()) {
	    MsgProperty pro = (MsgProperty) obj;
	    boolean bodyEqual = false;
	    boolean idEqual = false;
	    if (this.getWhichbody() == null) {
		if (pro.getWhichbody() == null)
		    bodyEqual = true;
	    } else {
		bodyEqual = this.getWhichbody().equals(pro.getWhichbody());
	    }
	    if (this.getId() == null) {
		if (pro.getId() == null)
		    idEqual = true;
	    } else {
		idEqual = this.getId().equals(pro.getId());
	    }
	    if (idEqual && this.isIfChose() == pro.isIfChose() && bodyEqual
		    && this.getRepeatSize() == pro.getRepeatSize()
		    && this.getUnNormalRepeatSize() == pro.getUnNormalRepeatSize()
		    && this.getLength() == pro.getLength()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public int hashCode() {
	int idHash = 0;
	if (this.getId() != null) {
	    idHash = this.getId().hashCode();
	}
	int ifChoseHash = Boolean.valueOf(this.isIfChose()).hashCode();
	int whichHash = 0;
	if (this.getWhichbody() != null) {
	    whichHash = this.getWhichbody().hashCode();
	}
	int repeatHash = Integer.valueOf(this.getRepeatSize()).hashCode();
	int unRepeatHash = Integer.valueOf(this.getUnNormalRepeatSize()).hashCode();
	int lengthHash = Integer.valueOf(this.getLength()).hashCode();
	return idHash * 31 + ifChoseHash * 32 + whichHash * 33 + repeatHash * 34 + unRepeatHash * 35 + lengthHash * 36;
    }
}
