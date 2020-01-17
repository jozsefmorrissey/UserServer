
-- ==========================  JUnit Test Data  =====================--
INSERT INTO UUSER_ABS (ID,FULLNAME,EMAIL,TOKEN) VALUES (-1,'ADMIN','jozsef.morrissey@yahoo.com','[B@d077e31');

INSERT INTO APP (ID, ACCESS_KEY, NAME) VALUES (1, '${appPassword}', 'USVC_SERVER');
INSERT INTO APP (ID, ACCESS_KEY, NAME) VALUES (-1, 'password', 'Defult_App');
INSERT INTO AUTHORIZED_APP_USER (APP_ID, USER_ID) VALUES (1, -1);

INSERT INTO RULE (ID, APP_ID, ENDPOINT_REG_EXP) VALUES (1, 1, ".*");

INSERT INTO UUSER_ABS (ID,FULLNAME,EMAIL,TOKEN) VALUES (1,'Jozsef Morrissey','jozsef.morrissey@gmail.com','[B@d077e31');
INSERT INTO UUSER_ABS (ID,FULLNAME,EMAIL,TOKEN) VALUES (2,'Jerad Morrissey','red3091@gmail.com','[B@d077e31');
INSERT INTO UUSER_ABS (ID,FULLNAME,EMAIL,TOKEN) VALUES (3,'Victor You Cant Handle This Quintanilla','vict.qp@gmail.com','[B@d077e31');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',4,'Calvin Singleton','gravida.mauris.ut@velitduisemper.org');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',5,'Haviva Hull','egestas.blandit@Ut.co.uk');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',6,'Amal Figueroa','In.faucibus@velfaucibus.ca');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',7,'Sade Reid','Pellentesque.ultricies.dignissim@ategestas.co.uk');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',8,'Jared Estrada','convallis.ligula.Donec@estarcuac.org');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',9,'Lana Abbott','posuere@enim.edu');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',10,'Edan Chavez','dapibus@in.net');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',11,'Fletcher Crosby','nunc.sed.libero@elementumlorem.net');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',12,'Adrian Goodman','dictum.Phasellus@consectetuermauris.org');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',13,'Sean Alston','Nam.porttitor.scelerisque@Utsagittislobortis.net');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',14,'Emerald Horn','nibh.enim.gravida@CuraeDonectincidunt.com');
INSERT INTO UUSER_ABS (TOKEN,ID,FULLNAME,EMAIL) VALUES ('[B@d077e31',15,'Reese Jimenez','pellentesque.a@duinec.ca');

INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (1,1,124,'This is the skill that never ends. ',STR_TO_DATE('10-SEP-0214:10:10','%d-%b-%y%T'));
INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (2,1,124,'It goes on and on my friends. ',STR_TO_DATE('10-SEP-0214:33:10','%d-%b-%y%T'));
INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (3,1,124,'some people started concatinating not knowing what it was ',STR_TO_DATE('10-SEP-0214:14:13','%d-%b-%y%T'));
INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (4,1,124,'and they just kept concatinating forever just because',STR_TO_DATE('10-SEP-0214:10:12','%d-%b-%y%T'));

INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (5,2,124,'In all seriousness ',STR_TO_DATE('10-SEP-0214:10:10','%d-%b-%y%T'));
INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (6,2,124,'I think this is a powerful tool that is worth the trouble. ',STR_TO_DATE('10-SEP-0214:10:14','%d-%b-%y%T'));
INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (7,2,124,'Now users can provide as much or as little information as they want. ',STR_TO_DATE('10-SEP-0214:16:10','%d-%b-%y%T'));
INSERT INTO REMARK (ID,POSTER_ID, CONVERSATION_ID, CONTENT, TIME_STAMP) VALUES (8,2,124,'Without harsh memory costs to the system... Enjoy!',STR_TO_DATE('10-SEP-0214:15:10','%d-%b-%y%T'));

INSERT INTO PERMISSION (ID, USER_ID, REF_TYPE, REF_ID, TYPE, ORIGIN_USER_ID) VALUES (-1, -1, '__admin__', -1, 'oao', null);
INSERT INTO PERMISSION (ID, USER_ID, REF_TYPE, REF_ID, TYPE, ORIGIN_USER_ID, APP_USER_ID) VALUES (1, 2, 'object', 3, 'oao', null,1);
INSERT INTO PERMISSION (ID, USER_ID, REF_TYPE, REF_ID, TYPE, ORIGIN_USER_ID, APP_USER_ID) VALUES (2, 4, 'object', 3, 'psuedo', null,1);
INSERT INTO PERMISSION (ID, USER_ID, REF_TYPE, REF_ID, TYPE, ORIGIN_USER_ID, APP_USER_ID) VALUES (3, 7, 'object', 3, 'validation', 4,1);
