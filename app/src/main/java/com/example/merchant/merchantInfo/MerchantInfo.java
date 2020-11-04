package com.example.merchant.merchantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
//
//public class MerchantInfo implements Parcelable, Serializable {
//    public String statusCode;
//    public Message message;
//
//    protected MerchantInfo(Parcel in) {
//        statusCode = in.readString();
//        message = in.readParcelable(Message.class.getClassLoader());
//    }
//
//    public static final Creator<MerchantInfo> CREATOR = new Creator<MerchantInfo>() {
//        @Override
//        public MerchantInfo createFromParcel(Parcel in) {
//            return new MerchantInfo(in);
//        }
//
//        @Override
//        public MerchantInfo[] newArray(int size) {
//            return new MerchantInfo[size];
//        }
//    };
//
//    public String getStatusCode() {
//        return statusCode;
//    }
//
//    public void setStatusCode(String statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public Message getMessage() {
//        return message;
//    }
//
//    public void setMessage(Message message) {
//        this.message = message;
//    }
//
//    @Override
//    public String toString() {
//        return "MerchantInfo{" +
//                "statusCode='" + statusCode + '\'' +
//                ", message=" + message +
//                '}';
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(statusCode);
//        parcel.writeParcelable(message, i);
//    }
//}
//
//class ImageArr implements Serializable,Parcelable {
//    public int imageId;
//    public String imageAddress;
//
//    protected ImageArr(Parcel in) {
//        imageId = in.readInt();
//        imageAddress = in.readString();
//    }
//
//    public static final Creator<ImageArr> CREATOR = new Creator<ImageArr>() {
//        @Override
//        public ImageArr createFromParcel(Parcel in) {
//            return new ImageArr(in);
//        }
//
//        @Override
//        public ImageArr[] newArray(int size) {
//            return new ImageArr[size];
//        }
//    };
//
//    public int getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }
//
//    public String getImageAddress() {
//        return imageAddress;
//    }
//
//    public void setImageAddress(String imageAddress) {
//        this.imageAddress = imageAddress;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(imageId);
//        parcel.writeString(imageAddress);
//    }
//}
//
//
//class Message implements Parcelable,Serializable {
//    public int merchantId;
//    public String merchantName;
//    public String merchantType;
//    public List<ImageArr> imageArr;
//    public int distance;
//    public String email;
//    public String phoneNumber;
//
//    protected Message(Parcel in) {
//        merchantId = in.readInt();
//        merchantName = in.readString();
//        merchantType = in.readString();
//        distance = in.readInt();
//        email = in.readString();
//        phoneNumber = in.readString();
//    }
//
//    public static final Creator<Message> CREATOR = new Creator<Message>() {
//        @Override
//        public Message createFromParcel(Parcel in) {
//            return new Message(in);
//        }
//
//        @Override
//        public Message[] newArray(int size) {
//            return new Message[size];
//        }
//    };
//
//    public int getMerchantId() {
//        return merchantId;
//    }
//
//    public void setMerchantId(int merchantId) {
//        this.merchantId = merchantId;
//    }
//
//    public String getMerchantName() {
//        return merchantName;
//    }
//
//    public void setMerchantName(String merchantName) {
//        this.merchantName = merchantName;
//    }
//
//    public String getMerchantType() {
//        return merchantType;
//    }
//
//    public void setMerchantType(String merchantType) {
//        this.merchantType = merchantType;
//    }
//
//    public List<ImageArr> getImageArr() {
//        return imageArr;
//    }
//
//    public void setImageArr(List<ImageArr> imageArr) {
//        this.imageArr = imageArr;
//    }
//
//    public int getDistance() {
//        return distance;
//    }
//
//    public void setDistance(int distance) {
//        this.distance = distance;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(merchantId);
//        parcel.writeString(merchantName);
//        parcel.writeString(merchantType);
//        parcel.writeInt(distance);
//        parcel.writeString(email);
//        parcel.writeString(phoneNumber);
//    }
//}

//
//class ImageArr implements Parcelable, Serializable {
//    public int imageId;
//    public String imageAddress;
//
//    public ImageArr(int imageId, String imageAddress) {
//        this.imageId = imageId;
//        this.imageAddress = imageAddress;
//    }
//
//    protected ImageArr(Parcel in) {
//        imageId = in.readInt();
//        imageAddress = in.readString();
//    }
//
//    public static final Creator<ImageArr> CREATOR = new Creator<ImageArr>() {
//        @Override
//        public ImageArr createFromParcel(Parcel in) {
//            return new ImageArr(in);
//        }
//
//        @Override
//        public ImageArr[] newArray(int size) {
//            return new ImageArr[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(imageId);
//        parcel.writeString(imageAddress);
//    }
//}
//
//class Location implements Parcelable, Serializable {
//    public int locationId;
//    public double lat;
//    public double lng;
//
//    public Location(int locationId, double lat, double lng) {
//        this.locationId = locationId;
//        this.lat = lat;
//        this.lng = lng;
//    }
//
//    protected Location(Parcel in) {
//        locationId = in.readInt();
//        lat = in.readDouble();
//        lng = in.readDouble();
//    }
//
//    public static final Creator<Location> CREATOR = new Creator<Location>() {
//        @Override
//        public Location createFromParcel(Parcel in) {
//            return new Location(in);
//        }
//
//        @Override
//        public Location[] newArray(int size) {
//            return new Location[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(locationId);
//        parcel.writeDouble(lat);
//        parcel.writeDouble(lng);
//    }
//}
//
//class Message implements Parcelable, Serializable {
//
//    public int getMerchantId() {
//        return merchantId;
//    }
//
//    public void setMerchantId(int merchantId) {
//        this.merchantId = merchantId;
//    }
//
//    public String getMerchantName() {
//        return merchantName;
//    }
//
//    public void setMerchantName(String merchantName) {
//        this.merchantName = merchantName;
//    }
//
//    public String getMerchantType() {
//        return merchantType;
//    }
//
//    public void setMerchantType(String merchantType) {
//        this.merchantType = merchantType;
//    }
//
//    public List<ImageArr> getImageArr() {
//        return imageArr;
//    }
//
//    public void setImageArr(List<ImageArr> imageArr) {
//        this.imageArr = imageArr;
//    }
//
//    public int getDistance() {
//        return distance;
//    }
//
//    public void setDistance(int distance) {
//        this.distance = distance;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public Location getLocation() {
//        return location;
//    }
//
//    public void setLocation(Location location) {
//        this.location = location;
//    }
//
//    public double getAverageRating() {
//        return averageRating;
//    }
//
//    public void setAverageRating(double averageRating) {
//        this.averageRating = averageRating;
//    }
//
//    public int getTotalRatings() {
//        return totalRatings;
//    }
//
//    public void setTotalRatings(int totalRatings) {
//        this.totalRatings = totalRatings;
//    }
//
//    public Message(int merchantId, String merchantName, String merchantType, List<ImageArr> imageArr, int distance, String email, String phoneNumber, Location location, double averageRating, int totalRatings) {
//        this.merchantId = merchantId;
//        this.merchantName = merchantName;
//        this.merchantType = merchantType;
//        this.imageArr = imageArr;
//        this.distance = distance;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.location = location;
//        this.averageRating = averageRating;
//        this.totalRatings = totalRatings;
//    }
//
//    public int merchantId;
//    public String merchantName;
//    public String merchantType;
//    public List<ImageArr> imageArr;
//    public int distance;
//    public String email;
//    public String phoneNumber;
//    public Location location;
//    public double averageRating;
//    public int totalRatings;
//
//    protected Message(Parcel in) {
//        merchantId = in.readInt();
//        merchantName = in.readString();
//        merchantType = in.readString();
//        distance = in.readInt();
//        email = in.readString();
//        phoneNumber = in.readString();
//        averageRating = in.readDouble();
//        totalRatings = in.readInt();
//    }
//
//    public static final Creator<Message> CREATOR = new Creator<Message>() {
//        @Override
//        public Message createFromParcel(Parcel in) {
//            return new Message(in);
//        }
//
//        @Override
//        public Message[] newArray(int size) {
//            return new Message[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeInt(merchantId);
//        parcel.writeString(merchantName);
//        parcel.writeString(merchantType);
//        parcel.writeInt(distance);
//        parcel.writeString(email);
//        parcel.writeString(phoneNumber);
//        parcel.writeDouble(averageRating);
//        parcel.writeInt(totalRatings);
//    }
//}
//
//public class MerchantInfo implements Parcelable, Serializable {
//    public String statusCode;
//    public Message message;
//
//    public String getStatusCode() {
//        return statusCode;
//    }
//
//    public void setStatusCode(String statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public Message getMessage() {
//        return message;
//    }
//
//    public void setMessage(Message message) {
//        this.message = message;
//    }
//
//    protected MerchantInfo(Parcel in) {
//        statusCode = in.readString();
//    }
//
//    public static final Creator<MerchantInfo> CREATOR = new Creator<MerchantInfo>() {
//        @Override
//        public MerchantInfo createFromParcel(Parcel in) {
//            return new MerchantInfo(in);
//        }
//
//        @Override
//        public MerchantInfo[] newArray(int size) {
//            return new MerchantInfo[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(statusCode);
//    }
//}
//
//
//







public class MerchantInfo  implements Parcelable, Serializable {
    public String statusCode;
    public Message message;

    public MerchantInfo(String statusCode, Message message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    protected MerchantInfo(Parcel in) {
        statusCode = in.readString();
        message = in.readParcelable(Message.class.getClassLoader());
    }

    public static final Creator<MerchantInfo> CREATOR = new Creator<MerchantInfo>() {
        @Override
        public MerchantInfo createFromParcel(Parcel in) {
            return new MerchantInfo(in);
        }

        @Override
        public MerchantInfo[] newArray(int size) {
            return new MerchantInfo[size];
        }
    };

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(statusCode);
        parcel.writeParcelable(message, i);
    }
}










