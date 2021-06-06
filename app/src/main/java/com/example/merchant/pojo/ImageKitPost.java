package com.example.merchant.pojo;

public class ImageKitPost {
    /*
    --header 'Content-Type: application/x-www-form-urlencoded' \
--form 'file="data:image/jpeg;base64,encodedimagehere"' \
--form 'fileName="file-name.jpg"' \
--form 'tags="sample-tag"' \
--form 'signature="88fa0f591bfeb924764a209b90a70f9819f2ce20"' \
--form 'publicKey="public_ks+Di7EZRiH4Yd8qP0b9UPnEIOs="' \
--form 'token="acfb8ebf-e20b-418e-afc2-22452bc8de93"' \
--form 'expire="1607282927"' \
--form 'folder="dummy-folder”'
     */

    String file;
    String fileName;
    String tags, signature, publicKey, token, folder;
    long expire;

    public ImageKitPost(String fileName, String signature, String publicKey, String token, long expire, String folder, String file) {
        this.fileName = fileName;
        this.signature = signature;
        this.publicKey = publicKey;
        this.token = token;
        this.expire = expire;
        this.folder = folder;
        this.file = file;
        tags = "sample-tag";
        System.out.println(toString()+"ress");
    }

    @Override
    public String toString() {
        return "imageKitPost{" +
                "file='" + file + '\'' +
                ", fileName='" + fileName + '\'' +
                ", tags='" + tags + '\'' +
                ", signature='" + signature + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", token='" + token + '\'' +
                ", folder='" + folder + '\'' +
                ", expire=" + expire +
                '}';
    }
}
