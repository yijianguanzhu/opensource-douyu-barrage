/*
 * Copyright (c) 2021-2031, yijianguanzhu (yijianguanzhu@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
package com.yijianguanzhu.douyu.barrage.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author yijianguanzhu 2020年7月8日
 */
public final class Message2BeanUtil {

	public final static String AT = "@";
	public final static String ATA = "@A";
	public final static String ATS = "@S";
	public final static String SLASH = "/";
	public final static String ATSIGN = "@=";
	public final static String COLON = ":";
	public final static String MARK = "\"";
	public final static String MARKS = "\\\\\"";
	public final static String COMMA = ",";
	public final static String TXT = "\"txt\"";
	public final static ObjectMapper JACKSONMAPPER = new ObjectMapper();
	static {
		JACKSONMAPPER.setSerializationInclusion( Include.NON_NULL );
		JACKSONMAPPER.configure( DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false );
		JACKSONMAPPER.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
		JACKSONMAPPER.configure( Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );
	}

	public static <T> T bean( ByteBuffer msg, Class<T> clazz ) throws IOException {
		return JACKSONMAPPER.readValue( toJsonString( msg ), clazz );
	}

	public static <T> T bean( byte[] msg, Class<T> clazz ) throws IOException {
		return JACKSONMAPPER.readValue( toJsonString( msg ), clazz );
	}

	public static <T> T bean( String jsonString, Class<T> clazz ) throws IOException {
		return JACKSONMAPPER.readValue( jsonString, clazz );
	}

	public static String toJsonString( ByteBuffer msg ) {
		return toJsonString( new String( msg.array(), StandardCharsets.UTF_8 ) );
	}

	public static String toJsonString( byte[] msg ) {
		return toJsonString( new String( msg, StandardCharsets.UTF_8 ) );
	}

	private static String toJsonString( String msg ) {
		StringBuilder builder = new StringBuilder( "{" );
		String[] split = msg.split( SLASH );

		for ( String str : split ) {
			String[] keyVal = str.split( ATSIGN );
			String key;
			builder.append( key = key( keyVal[0] ) ).append( COLON );
			if ( TXT.equals( key ) ) {
				builder.append( txtValue( keyVal[1] ) );
			}
			else {
				String value = keyVal.length > 1 ? keyVal[1] : "";
				if ( isJSON( value ) ) {
					builder.append( jsonValue( value ) );
				}
				else {
					builder.append( normalValue( value ) );
				}
			}
			builder.append( COMMA );
		}
		// 删除最后一个","逗号
		builder.deleteCharAt( builder.length() - 1 );
		builder.append( "}" );
		return builder.toString();
	}

	private static String deserialize( String msg ) {
		if ( !msg.isEmpty() && ( msg.contains( ATS ) || msg.contains( ATA ) ) ) {
			return deserialize( msg.replaceAll( ATA, AT ).replaceAll( ATS, SLASH ) );
		}
		return msg;
	}

	private static String key( String msg ) {
		return MARK + msg + MARK;
	}

	/**
	 * 简单地判断数据是不是json字符串
	 */
	private static boolean isJSON( String msg ) {
		msg = msg.trim();
		if ( msg.startsWith( "{" ) && msg.endsWith( "}" ) ) {
			return true;
		}
		if ( msg.startsWith( "[" ) && msg.endsWith( "]" ) ) {
			return true;
		}
		return false;
	}

	/**
	 * 用户发言文本处理
	 */
	private static String txtValue( String msg ) {
		if ( msg.contains( MARK ) ) {
			msg = msg.replaceAll( MARK, MARKS );
		}
		return MARK + msg.replaceAll( ATA, AT ) + MARK;
	}

	/**
	 * 通用字符串处理
	 */
	private static String normalValue( String msg ) {
		return MARK + deserialize( msg ) + MARK;
	}

	/**
	 * json字符串处理
	 */
	private static String jsonValue( String msg ) {
		return deserialize( msg );
	}
}
