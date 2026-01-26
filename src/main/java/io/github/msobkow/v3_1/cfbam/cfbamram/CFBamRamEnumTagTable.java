
// Description: Java 25 in-memory RAM DbIO implementation for EnumTag.

/*
 *	io.github.msobkow.CFBam
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFBam - Business Application Model
 *	
 *	This file is part of Mark's Code Fractal CFBam.
 *	
 *	Mark's Code Fractal CFBam is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU General Public License,
 *	Version 3 or later.
 *	
 *	Mark's Code Fractal CFBam is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFBam is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with Mark's Code Fractal CFBam.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 *	
 */

package io.github.msobkow.v3_1.cfbam.cfbamram;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfsec.cfsec.buff.*;
import io.github.msobkow.v3_1.cfint.cfint.buff.*;
import io.github.msobkow.v3_1.cfbam.cfbam.buff.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;

/*
 *	CFBamRamEnumTagTable in-memory RAM DbIO implementation
 *	for EnumTag.
 */
public class CFBamRamEnumTagTable
	implements ICFBamEnumTagTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffEnumTag > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffEnumTag >();
	private Map< CFBamBuffEnumTagByEnumIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByEnumIdx
		= new HashMap< CFBamBuffEnumTagByEnumIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();
	private Map< CFBamBuffEnumTagByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffEnumTagByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();
	private Map< CFBamBuffEnumTagByEnumNameIdxKey,
			CFBamBuffEnumTag > dictByEnumNameIdx
		= new HashMap< CFBamBuffEnumTagByEnumNameIdxKey,
			CFBamBuffEnumTag >();
	private Map< CFBamBuffEnumTagByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByPrevIdx
		= new HashMap< CFBamBuffEnumTagByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();
	private Map< CFBamBuffEnumTagByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >> dictByNextIdx
		= new HashMap< CFBamBuffEnumTagByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffEnumTag >>();

	public CFBamRamEnumTagTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamEnumTag createEnumTag( ICFSecAuthorization Authorization,
		ICFBamEnumTag Buff )
	{
		final String S_ProcName = "createEnumTag";
			ICFBamEnumTag tail = null;

			ICFBamEnumTag[] siblings = schema.getTableEnumTag().readDerivedByEnumIdx( Authorization,
				Buff.getRequiredEnumId() );
			for( int idx = 0; ( tail == null ) && ( idx < siblings.length ); idx ++ ) {
				if( ( siblings[idx].getOptionalNextId() == null ) )
				{
					tail = siblings[idx];
				}
			}
			if( tail != null ) {
				Buff.setOptionalPrevId( tail.getRequiredId() );
			}
			else {
				Buff.setOptionalPrevId( null );
			}
		
		CFLibDbKeyHash256 pkey;
		pkey = schema.nextEnumTagIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffEnumTagByEnumIdxKey keyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getFactoryEnumTag().newByEnumIdxKey();
		keyEnumIdx.setRequiredEnumId( Buff.getRequiredEnumId() );

		CFBamBuffEnumTagByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getFactoryEnumTag().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByEnumNameIdxKey keyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getFactoryEnumTag().newByEnumNameIdxKey();
		keyEnumNameIdx.setRequiredEnumId( Buff.getRequiredEnumId() );
		keyEnumNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffEnumTagByPrevIdxKey keyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getFactoryEnumTag().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffEnumTagByNextIdxKey keyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getFactoryEnumTag().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByEnumNameIdx.containsKey( keyEnumNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"EnumTagEnumNameIdx",
				"EnumTagEnumNameIdx",
				keyEnumNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredEnumId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"EnumDef",
						"EnumDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictEnumIdx;
		if( dictByEnumIdx.containsKey( keyEnumIdx ) ) {
			subdictEnumIdx = dictByEnumIdx.get( keyEnumIdx );
		}
		else {
			subdictEnumIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByEnumIdx.put( keyEnumIdx, subdictEnumIdx );
		}
		subdictEnumIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByEnumNameIdx.put( keyEnumNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamEnumTag tailEdit = schema.getFactoryEnumTag().newBuff();
			tailEdit.set( (ICFBamEnumTag)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableEnumTag().updateEnumTag( Authorization, tailEdit );
		}
		return( Buff );
	}

	public ICFBamEnumTag readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerived";
		ICFBamEnumTag buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamEnumTag lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerived";
		ICFBamEnumTag buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamEnumTag[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamEnumTag.readAllDerived";
		ICFBamEnumTag[] retList = new ICFBamEnumTag[ dictByPKey.values().size() ];
		Iterator< ICFBamEnumTag > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamEnumTag[] readDerivedByEnumIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumIdx";
		CFBamBuffEnumTagByEnumIdxKey key = (CFBamBuffEnumTagByEnumIdxKey)schema.getFactoryEnumTag().newByEnumIdxKey();
		key.setRequiredEnumId( EnumId );

		ICFBamEnumTag[] recArray;
		if( dictByEnumIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictEnumIdx
				= dictByEnumIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictEnumIdx.size() ];
			Iterator< ICFBamEnumTag > iter = subdictEnumIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictEnumIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByEnumIdx.put( key, subdictEnumIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	public ICFBamEnumTag[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByDefSchemaIdx";
		CFBamBuffEnumTagByDefSchemaIdxKey key = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getFactoryEnumTag().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamEnumTag[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamEnumTag > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	public ICFBamEnumTag readDerivedByEnumNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId,
		String Name )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByEnumNameIdx";
		CFBamBuffEnumTagByEnumNameIdxKey key = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getFactoryEnumTag().newByEnumNameIdxKey();
		key.setRequiredEnumId( EnumId );
		key.setRequiredName( Name );

		ICFBamEnumTag buff;
		if( dictByEnumNameIdx.containsKey( key ) ) {
			buff = dictByEnumNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamEnumTag[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByPrevIdx";
		CFBamBuffEnumTagByPrevIdxKey key = (CFBamBuffEnumTagByPrevIdxKey)schema.getFactoryEnumTag().newByPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		ICFBamEnumTag[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictPrevIdx.size() ];
			Iterator< ICFBamEnumTag > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	public ICFBamEnumTag[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByNextIdx";
		CFBamBuffEnumTagByNextIdxKey key = (CFBamBuffEnumTagByNextIdxKey)schema.getFactoryEnumTag().newByNextIdxKey();
		key.setOptionalNextId( NextId );

		ICFBamEnumTag[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamEnumTag[ subdictNextIdx.size() ];
			Iterator< ICFBamEnumTag > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamEnumTag[0];
		}
		return( recArray );
	}

	public ICFBamEnumTag readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamEnumTag.readDerivedByIdIdx() ";
		ICFBamEnumTag buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamEnumTag readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuff";
		ICFBamEnumTag buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamEnumTag.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamEnumTag lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamEnumTag buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamEnumTag.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamEnumTag[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamEnumTag.readAllBuff";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	public ICFBamEnumTag readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByIdIdx() ";
		ICFBamEnumTag buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
			return( (ICFBamEnumTag)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamEnumTag[] readBuffByEnumIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByEnumIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByEnumIdx( Authorization,
			EnumId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	public ICFBamEnumTag[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByDefSchemaIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	public ICFBamEnumTag readBuffByEnumNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 EnumId,
		String Name )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByEnumNameIdx() ";
		ICFBamEnumTag buff = readDerivedByEnumNameIdx( Authorization,
			EnumId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
			return( (ICFBamEnumTag)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamEnumTag[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByPrevIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	public ICFBamEnumTag[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamEnumTag.readBuffByNextIdx() ";
		ICFBamEnumTag buff;
		ArrayList<ICFBamEnumTag> filteredList = new ArrayList<ICFBamEnumTag>();
		ICFBamEnumTag[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamEnumTag.CLASS_CODE ) ) {
				filteredList.add( (ICFBamEnumTag)buff );
			}
		}
		return( filteredList.toArray( new ICFBamEnumTag[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamEnumTag moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamEnumTag grandprev = null;
		ICFBamEnumTag prev = null;
		ICFBamEnumTag cur = null;
		ICFBamEnumTag next = null;

		cur = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamEnumTagBuff)cur );
		}

		prev = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		ICFBamEnumTag newInstance;
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		ICFBamEnumTag editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editCur = newInstance;
		editCur.set( cur );

		ICFBamEnumTag editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		ICFBamEnumTag editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext = newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalNextId( cur.getRequiredId() );
			editCur.setOptionalPrevId( grandprev.getRequiredId() );
		}
		else {
			editCur.setOptionalPrevId( null );
		}

			editPrev.setOptionalPrevId( cur.getRequiredId() );

			editCur.setOptionalNextId( prev.getRequiredId() );

		if( next != null ) {
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editPrev.setOptionalNextId( null );
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamEnumTagBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamEnumTag moveBuffDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamEnumTagBuff prev = null;
		CFBamEnumTagBuff cur = null;
		CFBamEnumTagBuff next = null;
		CFBamEnumTagBuff grandnext = null;

		cur = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamEnumTagBuff)cur );
		}

		next = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableEnumTag().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamEnumTagBuff newInstance;
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamEnumTagBuff editNext = newInstance;
		editNext.set( next );

		CFBamEnumTagBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamEnumTagBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				newInstance = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev = newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editNext.setOptionalPrevId( null );
		}

			editCur.setOptionalPrevId( next.getRequiredId() );

			editNext.setOptionalNextId( cur.getRequiredId() );

		if( editGrandnext != null ) {
			editCur.setOptionalNextId( grandnext.getRequiredId() );
			editGrandnext.setOptionalPrevId( cur.getRequiredId() );
		}
		else {
			editCur.setOptionalNextId( null );
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamEnumTagBuff)editCur );
	}

	public ICFBamEnumTag updateEnumTag( ICFSecAuthorization Authorization,
		ICFBamEnumTag Buff )
	{
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamEnumTag existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateEnumTag",
				"Existing record not found",
				"EnumTag",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateEnumTag",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffEnumTagByEnumIdxKey existingKeyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getFactoryEnumTag().newByEnumIdxKey();
		existingKeyEnumIdx.setRequiredEnumId( existing.getRequiredEnumId() );

		CFBamBuffEnumTagByEnumIdxKey newKeyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getFactoryEnumTag().newByEnumIdxKey();
		newKeyEnumIdx.setRequiredEnumId( Buff.getRequiredEnumId() );

		CFBamBuffEnumTagByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getFactoryEnumTag().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getFactoryEnumTag().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByEnumNameIdxKey existingKeyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getFactoryEnumTag().newByEnumNameIdxKey();
		existingKeyEnumNameIdx.setRequiredEnumId( existing.getRequiredEnumId() );
		existingKeyEnumNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffEnumTagByEnumNameIdxKey newKeyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getFactoryEnumTag().newByEnumNameIdxKey();
		newKeyEnumNameIdx.setRequiredEnumId( Buff.getRequiredEnumId() );
		newKeyEnumNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffEnumTagByPrevIdxKey existingKeyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getFactoryEnumTag().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffEnumTagByPrevIdxKey newKeyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getFactoryEnumTag().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffEnumTagByNextIdxKey existingKeyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getFactoryEnumTag().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffEnumTagByNextIdxKey newKeyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getFactoryEnumTag().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyEnumNameIdx.equals( newKeyEnumNameIdx ) ) {
			if( dictByEnumNameIdx.containsKey( newKeyEnumNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateEnumTag",
					"EnumTagEnumNameIdx",
					"EnumTagEnumNameIdx",
					newKeyEnumNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredEnumId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateEnumTag",
						"Container",
						"EnumDef",
						"EnumDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByEnumIdx.get( existingKeyEnumIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByEnumIdx.containsKey( newKeyEnumIdx ) ) {
			subdict = dictByEnumIdx.get( newKeyEnumIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByEnumIdx.put( newKeyEnumIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByEnumNameIdx.remove( existingKeyEnumNameIdx );
		dictByEnumNameIdx.put( newKeyEnumNameIdx, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByPrevIdx.put( newKeyPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextIdx.get( existingKeyNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextIdx.containsKey( newKeyNextIdx ) ) {
			subdict = dictByNextIdx.get( newKeyNextIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffEnumTag >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteEnumTag( ICFSecAuthorization Authorization,
		ICFBamEnumTag Buff )
	{
		final String S_ProcName = "CFBamRamEnumTagTable.deleteEnumTag() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryEnumTag().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamEnumTag existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteEnumTag",
				pkey );
		}
		CFLibDbKeyHash256 varEnumId = existing.getRequiredEnumId();
		CFBamEnumDefBuff container = schema.getTableEnumDef().readDerivedByIdIdx( Authorization,
			varEnumId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamEnumTagBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamEnumTagBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				editPrev = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamEnumTagBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamEnumTagBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a81e" ) ) {
				editNext = schema.getFactoryEnumTag().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a81e" ) ) {
				schema.getTableEnumTag().updateEnumTag( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamBuffEnumTagByEnumIdxKey keyEnumIdx = (CFBamBuffEnumTagByEnumIdxKey)schema.getFactoryEnumTag().newByEnumIdxKey();
		keyEnumIdx.setRequiredEnumId( existing.getRequiredEnumId() );

		CFBamBuffEnumTagByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getFactoryEnumTag().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffEnumTagByEnumNameIdxKey keyEnumNameIdx = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getFactoryEnumTag().newByEnumNameIdxKey();
		keyEnumNameIdx.setRequiredEnumId( existing.getRequiredEnumId() );
		keyEnumNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffEnumTagByPrevIdxKey keyPrevIdx = (CFBamBuffEnumTagByPrevIdxKey)schema.getFactoryEnumTag().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffEnumTagByNextIdxKey keyNextIdx = (CFBamBuffEnumTagByNextIdxKey)schema.getFactoryEnumTag().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffEnumTag > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByEnumIdx.get( keyEnumIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByEnumNameIdx.remove( keyEnumNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

	}
	public void deleteEnumTagByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamEnumTag cur;
		LinkedList<ICFBamEnumTag> matchSet = new LinkedList<ICFBamEnumTag>();
		Iterator<ICFBamEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByEnumIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argEnumId )
	{
		CFBamBuffEnumTagByEnumIdxKey key = (CFBamBuffEnumTagByEnumIdxKey)schema.getFactoryEnumTag().newByEnumIdxKey();
		key.setRequiredEnumId( argEnumId );
		deleteEnumTagByEnumIdx( Authorization, key );
	}

	public void deleteEnumTagByEnumIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByEnumIdxKey argKey )
	{
		ICFBamEnumTag cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamEnumTag> matchSet = new LinkedList<ICFBamEnumTag>();
		Iterator<ICFBamEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffEnumTagByDefSchemaIdxKey key = (CFBamBuffEnumTagByDefSchemaIdxKey)schema.getFactoryEnumTag().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteEnumTagByDefSchemaIdx( Authorization, key );
	}

	public void deleteEnumTagByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByDefSchemaIdxKey argKey )
	{
		ICFBamEnumTag cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamEnumTag> matchSet = new LinkedList<ICFBamEnumTag>();
		Iterator<ICFBamEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByEnumNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argEnumId,
		String argName )
	{
		CFBamBuffEnumTagByEnumNameIdxKey key = (CFBamBuffEnumTagByEnumNameIdxKey)schema.getFactoryEnumTag().newByEnumNameIdxKey();
		key.setRequiredEnumId( argEnumId );
		key.setRequiredName( argName );
		deleteEnumTagByEnumNameIdx( Authorization, key );
	}

	public void deleteEnumTagByEnumNameIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByEnumNameIdxKey argKey )
	{
		ICFBamEnumTag cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamEnumTag> matchSet = new LinkedList<ICFBamEnumTag>();
		Iterator<ICFBamEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffEnumTagByPrevIdxKey key = (CFBamBuffEnumTagByPrevIdxKey)schema.getFactoryEnumTag().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteEnumTagByPrevIdx( Authorization, key );
	}

	public void deleteEnumTagByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByPrevIdxKey argKey )
	{
		ICFBamEnumTag cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamEnumTag> matchSet = new LinkedList<ICFBamEnumTag>();
		Iterator<ICFBamEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}

	public void deleteEnumTagByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffEnumTagByNextIdxKey key = (CFBamBuffEnumTagByNextIdxKey)schema.getFactoryEnumTag().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteEnumTagByNextIdx( Authorization, key );
	}

	public void deleteEnumTagByNextIdx( ICFSecAuthorization Authorization,
		ICFBamEnumTagByNextIdxKey argKey )
	{
		ICFBamEnumTag cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamEnumTag> matchSet = new LinkedList<ICFBamEnumTag>();
		Iterator<ICFBamEnumTag> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamEnumTag> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableEnumTag().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteEnumTag( Authorization, cur );
		}
	}
}
