# The app is using in order to manage news and comments.

---

This app build on maven with java 8.<br/>

To run app use on of this commands inside cmd. By default, profile set as 'test'.


```bash
mvn clean install
```

```bash
mvn clean install -P "{prod or test}" -D "datasource.driver={datasource.driver}" -D "datasource.url={datasource.url}" -D "datasource.username={datasource.username}" -D "datasource.password={datasource.password}"
```
You can pass parameters to command for this app from below list:<br/>
- datasource.driver
- datasource.url
- datasource.username
- datasource.password
- hibernate.hbm2ddl.auto ( default 'none' )
- hibernate.show-sql ( default 'true' )
- hibernate.dialect ( default 'org.hibernate.dialect.PostgreSQLDialect' )

For profile 'test' all parameters already provided by default connection to postgres on localhost, but you can change it.<br/>

### !!! All data bounded to datasource parameters must be provided to profile 'prod'.

---

## For news access there's 5 endpoints below :

1) GET /news?page=1&size=20&searchText={searchText} { text by witch you want to search some news }
2) GET /news/{newsId}?page=1&size=20 { news's id which you want to get, page and size for paginating comments }
3) POST /news<br/> 
    For creating news with request body as:
```json
{ 
    "title": "title",
    "text": "text"
}
```
4) PATCH /news/{newsId} { news's id which you want to update }<br/>
   For updating news with request body as:
```json
{ 
    "title": "title",
    "text": "text"
}
```
5) DELETE /news/{newsId} { news's id which you want to delete }<br/>


---

## For comments access there's 4 endpoints below :
### All endpoint here have to has news id to bound comments with news

1) GET /news/{newsId}/comments/{commentsId} { comment's id which you want to get }
3) POST /news/{newsId}/comments<br/>
   For creating comments with request body as:
```json
{ "text": "text" }
```
4) PATCH /news/{newsId}/comments/{commentsId} { comment's id which you want to update }<br/>
   For updating comment with request body as:
```json
{ "text": "text" }
```
5) DELETE /news/{newsId}/comments/{commentsId} { news's id which you want to delete }<br/>