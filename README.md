# xml2json-converter

This application convert xml files to json files. The input dataset must be located in the folder: _src/main/resources/storage_.

At the time of conversion, the application saves the image files to folder _src/main/resources/images_ for files of type _Coah_.

There is also a functionality that allows you to combine all JSON files from the storage into one. There is a check for uniqueness by field _Giata_Id_.

### Storage

The application uses file storage:

- /storage - here xml and json files
- /images - all images
- /result - single Json object

**Note:** If you run the application from Intellij Idea, you can find these storages in _build/resources/main_.

## Rest API
###Init Convert XML2JSON and Extract Images:

&nbsp;&nbsp; **URL** : `/convert`

&nbsp;&nbsp; **Method** : `POST`

&nbsp;&nbsp; **Body** : Empty

&nbsp;&nbsp; **Response Code** : 202

&nbsp;&nbsp; **Response Body** :
```text
All xml documents was converted to JSON files.
```

###Get Single JSON object:

&nbsp;&nbsp; **URL** : `/result`

&nbsp;&nbsp; **Method** : `GET`

&nbsp;&nbsp; **Response Body** :
```json
    {
        // JSON Array
    }
```
