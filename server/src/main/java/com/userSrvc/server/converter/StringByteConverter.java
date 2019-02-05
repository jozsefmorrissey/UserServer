package com.userSrvc.server.converter;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringByteConverter implements AttributeConverter<String, byte[]>{

	public static void main(String...args) {
		System.out.println(Arrays.toString(new StringByteConverter().convertToDatabaseColumn("{\"content\": \"<pre>&lt;div class=\\\"center\\\"&gt;\\n  &lt;h2&gt;Git&lt;/h2&gt;\\n  &lt;label&gt;Level:&lt;/label&gt;\\n  &lt;select ng-model=\\\"filterValue\\\"&gt;\\n    &lt;option&gt;&lt;/option&gt;\\n    &lt;option ng-repeat='cmdObj in cmds | unique: \\\"level\\\"'&gt;{{cmdObj.level}}&lt;/option&gt;\\n  &lt;/select&gt; \\n  &lt;br&gt;\\n  &lt;div class='left' ng-if=\\\"filterValue\\\"&gt;\\n    &lt;div ng-repeat='cmdObj in cmds | filter:{level:filterValue}:true'&gt;\\n      &lt;b&gt;{{cmdObj.cmd}}&lt;/b&gt;\\n    &lt;/div&gt;\\n  &lt;/div&gt;\\n  &lt;div class='left' ng-if=\\\"!filterValue\\\"&gt;\\n    &lt;div ng-repeat='cmdObj in cmds'&gt;\\n      &lt;b&gt;{{cmdObj.cmd}}&lt;/b&gt;\\n    &lt;/div&gt;\\n  &lt;/div&gt;\\n&lt;/div&gt;</pre>\",\"data\": \"<div>{<br>&nbsp; \\\"cmds\\\": [{<br>&nbsp; &nbsp; \\\"cmd\\\": \\\"git add -f [filess]\\\",&nbsp;<br>&nbsp; &nbsp; \\\"level\\\": \\\"begginer\\\"<br>&nbsp; &nbsp;},{<br>&nbsp; &nbsp; \\\"cmd\\\": \\\"git rm -f [files]\\\",&nbsp;<br>&nbsp; &nbsp; \\\"level\\\": \\\"novice\\\"<br>&nbsp; },{<br>&nbsp; &nbsp; \\\"cmd\\\": \\\"git cp -f [files]\\\",&nbsp;<br>&nbsp; &nbsp; \\\"level\\\": \\\"novice\\\"<br>&nbsp; }]<br>}</div>\",\"links\": {\"git\": \"hub\",\"google\": \"http://www.google.com\"},\"css\": \"<div>.left {<br>&nbsp;width: fit-content;<br>&nbsp;margin: auto;<br>}&nbsp;<br><br></div>\",\"keywords\": {\"add\": \"<pre>Add&lt;!-- There is no related  content --&gt;</pre>\"},\"inheritedKeywords\": {}}")));
	}
	@Override
	public byte[] convertToDatabaseColumn(String arg0) {
		return arg0.getBytes();
	}

	@Override
	public String convertToEntityAttribute(byte[] arg0) {
		return new String(arg0);
	}
}
