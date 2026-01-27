
// Description: Java 25 in-memory RAM DbIO implementation for IndexCol.

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
 *	CFBamRamIndexColTable in-memory RAM DbIO implementation
 *	for IndexCol.
 */
public class CFBamRamIndexColTable
	implements ICFBamIndexColTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffIndexCol > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffIndexCol >();
	private Map< CFBamBuffIndexColByUNameIdxKey,
			CFBamBuffIndexCol > dictByUNameIdx
		= new HashMap< CFBamBuffIndexColByUNameIdxKey,
			CFBamBuffIndexCol >();
	private Map< CFBamBuffIndexColByIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByIndexIdx
		= new HashMap< CFBamBuffIndexColByIndexIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffIndexColByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByColIdx
		= new HashMap< CFBamBuffIndexColByColIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByPrevIdx
		= new HashMap< CFBamBuffIndexColByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByNextIdx
		= new HashMap< CFBamBuffIndexColByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByIdxPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByIdxPrevIdx
		= new HashMap< CFBamBuffIndexColByIdxPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();
	private Map< CFBamBuffIndexColByIdxNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >> dictByIdxNextIdx
		= new HashMap< CFBamBuffIndexColByIdxNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffIndexCol >>();

	public CFBamRamIndexColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffIndexCol ensureRec(ICFBamIndexCol rec) {
		if (rec == null) {
			return( null );
		}
		else {
			int classCode = rec.getClassCode();
			if (classCode == ICFBamIndexCol.CLASS_CODE) {
				return( ((CFBamBuffIndexColDefaultFactory)(schema.getFactoryIndexCol())).ensureRec(rec) );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), "ensureRec", 1, "rec", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamIndexCol createIndexCol( ICFSecAuthorization Authorization,
		ICFBamIndexCol iBuff )
	{
		final String S_ProcName = "createIndexCol";
		
		CFBamBuffIndexCol Buff = ensureRec(iBuff);
			ICFBamIndexCol tail = null;

			ICFBamIndexCol[] siblings = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				Buff.getRequiredIndexId() );
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
		pkey = schema.nextIndexColIdGen();
		Buff.setRequiredId( pkey );
		CFBamBuffIndexColByUNameIdxKey keyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getFactoryIndexCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexColByIndexIdxKey keyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getFactoryIndexCol().newByIndexIdxKey();
		keyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		CFBamBuffIndexColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getFactoryIndexCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffIndexColByColIdxKey keyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getFactoryIndexCol().newByColIdxKey();
		keyColIdx.setRequiredColumnId( Buff.getRequiredColumnId() );

		CFBamBuffIndexColByPrevIdxKey keyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getFactoryIndexCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByNextIdxKey keyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getFactoryIndexCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffIndexColByIdxPrevIdxKey keyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getFactoryIndexCol().newByIdxPrevIdxKey();
		keyIdxPrevIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyIdxPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByIdxNextIdxKey keyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getFactoryIndexCol().newByIdxNextIdxKey();
		keyIdxNextIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyIdxNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexColUNameIdx",
				"IndexColUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Index",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredColumnId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Column",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIndexIdx;
		if( dictByIndexIdx.containsKey( keyIndexIdx ) ) {
			subdictIndexIdx = dictByIndexIdx.get( keyIndexIdx );
		}
		else {
			subdictIndexIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIndexIdx.put( keyIndexIdx, subdictIndexIdx );
		}
		subdictIndexIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictColIdx;
		if( dictByColIdx.containsKey( keyColIdx ) ) {
			subdictColIdx = dictByColIdx.get( keyColIdx );
		}
		else {
			subdictColIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByColIdx.put( keyColIdx, subdictColIdx );
		}
		subdictColIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxPrevIdx;
		if( dictByIdxPrevIdx.containsKey( keyIdxPrevIdx ) ) {
			subdictIdxPrevIdx = dictByIdxPrevIdx.get( keyIdxPrevIdx );
		}
		else {
			subdictIdxPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxPrevIdx.put( keyIdxPrevIdx, subdictIdxPrevIdx );
		}
		subdictIdxPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxNextIdx;
		if( dictByIdxNextIdx.containsKey( keyIdxNextIdx ) ) {
			subdictIdxNextIdx = dictByIdxNextIdx.get( keyIdxNextIdx );
		}
		else {
			subdictIdxNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxNextIdx.put( keyIdxNextIdx, subdictIdxNextIdx );
		}
		subdictIdxNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamIndexCol tailEdit = schema.getFactoryIndexCol().newBuff();
			tailEdit.set( (ICFBamIndexCol)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableIndexCol().updateIndexCol( Authorization, tailEdit );
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamIndexCol.CLASS_CODE) {
				CFBamBuffIndexCol retbuff = ((CFBamBuffIndexCol)(schema.getFactoryIndexCol().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-create-buff-cloning-", "Not " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamIndexCol readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerived";
		ICFBamIndexCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndexCol lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerived";
		ICFBamIndexCol buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndexCol[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndexCol.readAllDerived";
		ICFBamIndexCol[] retList = new ICFBamIndexCol[ dictByPKey.values().size() ];
		Iterator< ICFBamIndexCol > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamIndexCol readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByUNameIdx";
		CFBamBuffIndexColByUNameIdxKey key = (CFBamBuffIndexColByUNameIdxKey)schema.getFactoryIndexCol().newByUNameIdxKey();
		key.setRequiredIndexId( IndexId );
		key.setRequiredName( Name );

		ICFBamIndexCol buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndexCol[] readDerivedByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIndexIdx";
		CFBamBuffIndexColByIndexIdxKey key = (CFBamBuffIndexColByIndexIdxKey)schema.getFactoryIndexCol().newByIndexIdxKey();
		key.setRequiredIndexId( IndexId );

		ICFBamIndexCol[] recArray;
		if( dictByIndexIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIndexIdx
				= dictByIndexIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictIndexIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIndexIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIndexIdx.put( key, subdictIndexIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByDefSchemaIdx";
		CFBamBuffIndexColByDefSchemaIdxKey key = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getFactoryIndexCol().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamIndexCol[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol[] readDerivedByColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ColumnId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByColIdx";
		CFBamBuffIndexColByColIdxKey key = (CFBamBuffIndexColByColIdxKey)schema.getFactoryIndexCol().newByColIdxKey();
		key.setRequiredColumnId( ColumnId );

		ICFBamIndexCol[] recArray;
		if( dictByColIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictColIdx
				= dictByColIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictColIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictColIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByColIdx.put( key, subdictColIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByPrevIdx";
		CFBamBuffIndexColByPrevIdxKey key = (CFBamBuffIndexColByPrevIdxKey)schema.getFactoryIndexCol().newByPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		ICFBamIndexCol[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictPrevIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByNextIdx";
		CFBamBuffIndexColByNextIdxKey key = (CFBamBuffIndexColByNextIdxKey)schema.getFactoryIndexCol().newByNextIdxKey();
		key.setOptionalNextId( NextId );

		ICFBamIndexCol[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictNextIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol[] readDerivedByIdxPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxPrevIdx";
		CFBamBuffIndexColByIdxPrevIdxKey key = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getFactoryIndexCol().newByIdxPrevIdxKey();
		key.setRequiredIndexId( IndexId );
		key.setOptionalPrevId( PrevId );

		ICFBamIndexCol[] recArray;
		if( dictByIdxPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxPrevIdx
				= dictByIdxPrevIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictIdxPrevIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictIdxPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxPrevIdx.put( key, subdictIdxPrevIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol[] readDerivedByIdxNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxNextIdx";
		CFBamBuffIndexColByIdxNextIdxKey key = (CFBamBuffIndexColByIdxNextIdxKey)schema.getFactoryIndexCol().newByIdxNextIdxKey();
		key.setRequiredIndexId( IndexId );
		key.setOptionalNextId( NextId );

		ICFBamIndexCol[] recArray;
		if( dictByIdxNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxNextIdx
				= dictByIdxNextIdx.get( key );
			recArray = new ICFBamIndexCol[ subdictIdxNextIdx.size() ];
			Iterator< ICFBamIndexCol > iter = subdictIdxNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdictIdxNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxNextIdx.put( key, subdictIdxNextIdx );
			recArray = new ICFBamIndexCol[0];
		}
		return( recArray );
	}

	public ICFBamIndexCol readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdIdx() ";
		ICFBamIndexCol buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndexCol readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuff";
		ICFBamIndexCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndexCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndexCol lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamIndexCol buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamIndexCol.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamIndexCol[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndexCol.readAllBuff";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdIdx() ";
		ICFBamIndexCol buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
			return( (ICFBamIndexCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamIndexCol readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByUNameIdx() ";
		ICFBamIndexCol buff = readDerivedByUNameIdx( Authorization,
			IndexId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
			return( (ICFBamIndexCol)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamIndexCol[] readBuffByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIndexIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByIndexIdx( Authorization,
			IndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByDefSchemaIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol[] readBuffByColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ColumnId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByColIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByColIdx( Authorization,
			ColumnId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByPrevIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByNextIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol[] readBuffByIdxPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdxPrevIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByIdxPrevIdx( Authorization,
			IndexId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	public ICFBamIndexCol[] readBuffByIdxNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 IndexId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdxNextIdx() ";
		ICFBamIndexCol buff;
		ArrayList<ICFBamIndexCol> filteredList = new ArrayList<ICFBamIndexCol>();
		ICFBamIndexCol[] buffList = readDerivedByIdxNextIdx( Authorization,
			IndexId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamIndexCol.CLASS_CODE ) ) {
				filteredList.add( (ICFBamIndexCol)buff );
			}
		}
		return( filteredList.toArray( new ICFBamIndexCol[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamIndexCol moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamIndexCol grandprev = null;
		ICFBamIndexCol prev = null;
		ICFBamIndexCol cur = null;
		ICFBamIndexCol next = null;

		cur = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamIndexColByIdIdxKey",
				"CFBamIndexColByIdIdxKey",
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamIndexColBuff)cur );
		}

		prev = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamIndexColByIdIdxKey",
				"CFBamIndexColByIdIdxKey",
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamIndexColByIdIdxKey",
					"CFBamIndexColByIdIdxKey",
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamIndexColByIdIdxKey",
					"CFBamIndexColByIdIdxKey",
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		ICFBamIndexCol newInstance;
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		ICFBamIndexCol editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamIndexColBuff editCur = newInstance;
		editCur.set( cur );

		ICFBamIndexCol editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		ICFBamIndexCol editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
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
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-grand-prev-", "Not " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-prev-", "Not " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-cur-", "Not " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-next-", "Not " + Integer.toString(classCode));
			}
		}

		return( (CFBamIndexColBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamIndexCol moveBuffDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamIndexColBuff prev = null;
		CFBamIndexColBuff cur = null;
		CFBamIndexColBuff next = null;
		CFBamIndexColBuff grandnext = null;

		cur = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamIndexColByIdIdxKey",
				"CFBamIndexColByIdIdxKey",
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamIndexColBuff)cur );
		}

		next = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamIndexColByIdIdxKey",
				"CFBamIndexColByIdIdxKey",
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamIndexColByIdIdxKey",
					"CFBamIndexColByIdIdxKey",
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamIndexColByIdIdxKey",
					"CFBamIndexColByIdIdxKey",
					"Could not locate object.prev" );
			}
		}

		integer classCode = cur.getClassCode();
		CFBamIndexColBuff newInstance;
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamIndexColBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
		CFBamIndexColBuff editNext = newInstance;
		editNext.set( next );

		CFBamIndexColBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamIndexColBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-instantiate-buff-", "Not " + Integer.toString(classCode));
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
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-prev-", "Not " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-cur-", "Not " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-next-", "Not " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamIndexCol.CLASS_CODE ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-update-grand-next-", "Not " + Integer.toString(classCode));
			}
		}

		return( (CFBamIndexColBuff)editCur );
	}

	public ICFBamIndexCol updateIndexCol( ICFSecAuthorization Authorization,
		ICFBamIndexCol Buff )
	{
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		ICFBamIndexCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndexCol",
				"Existing record not found",
				"IndexCol",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateIndexCol",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamBuffIndexColByUNameIdxKey existingKeyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getFactoryIndexCol().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexColByUNameIdxKey newKeyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getFactoryIndexCol().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffIndexColByIndexIdxKey existingKeyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getFactoryIndexCol().newByIndexIdxKey();
		existingKeyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamBuffIndexColByIndexIdxKey newKeyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getFactoryIndexCol().newByIndexIdxKey();
		newKeyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		CFBamBuffIndexColByDefSchemaIdxKey existingKeyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getFactoryIndexCol().newByDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffIndexColByDefSchemaIdxKey newKeyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getFactoryIndexCol().newByDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffIndexColByColIdxKey existingKeyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getFactoryIndexCol().newByColIdxKey();
		existingKeyColIdx.setRequiredColumnId( existing.getRequiredColumnId() );

		CFBamBuffIndexColByColIdxKey newKeyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getFactoryIndexCol().newByColIdxKey();
		newKeyColIdx.setRequiredColumnId( Buff.getRequiredColumnId() );

		CFBamBuffIndexColByPrevIdxKey existingKeyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getFactoryIndexCol().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByPrevIdxKey newKeyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getFactoryIndexCol().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByNextIdxKey existingKeyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getFactoryIndexCol().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffIndexColByNextIdxKey newKeyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getFactoryIndexCol().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffIndexColByIdxPrevIdxKey existingKeyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getFactoryIndexCol().newByIdxPrevIdxKey();
		existingKeyIdxPrevIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyIdxPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByIdxPrevIdxKey newKeyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getFactoryIndexCol().newByIdxPrevIdxKey();
		newKeyIdxPrevIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyIdxPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffIndexColByIdxNextIdxKey existingKeyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getFactoryIndexCol().newByIdxNextIdxKey();
		existingKeyIdxNextIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyIdxNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffIndexColByIdxNextIdxKey newKeyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getFactoryIndexCol().newByIdxNextIdxKey();
		newKeyIdxNextIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyIdxNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndexCol",
					"IndexColUNameIdx",
					"IndexColUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
						"Container",
						"Index",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredColumnId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
						"Lookup",
						"Column",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByIndexIdx.get( existingKeyIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIndexIdx.containsKey( newKeyIndexIdx ) ) {
			subdict = dictByIndexIdx.get( newKeyIndexIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIndexIdx.put( newKeyIndexIdx, subdict );
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByColIdx.get( existingKeyColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByColIdx.containsKey( newKeyColIdx ) ) {
			subdict = dictByColIdx.get( newKeyColIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByColIdx.put( newKeyColIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxPrevIdx.get( existingKeyIdxPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxPrevIdx.containsKey( newKeyIdxPrevIdx ) ) {
			subdict = dictByIdxPrevIdx.get( newKeyIdxPrevIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxPrevIdx.put( newKeyIdxPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxNextIdx.get( existingKeyIdxNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxNextIdx.containsKey( newKeyIdxNextIdx ) ) {
			subdict = dictByIdxNextIdx.get( newKeyIdxNextIdx );
		}
		else {
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffIndexCol >();
			dictByIdxNextIdx.put( newKeyIdxNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteIndexCol( ICFSecAuthorization Authorization,
		ICFBamIndexCol Buff )
	{
		final String S_ProcName = "CFBamRamIndexColTable.deleteIndexCol() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryIndexCol().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamIndexCol existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndexCol",
				pkey );
		}
		CFLibDbKeyHash256 varIndexId = existing.getRequiredIndexId();
		CFBamIndexBuff container = schema.getTableIndex().readDerivedByIdIdx( Authorization,
			varIndexId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamIndexColBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamIndexColBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				editPrev = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-update-prev-", "Not " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-edit-prev-", "Not " + Integer.toString(classCode));
			}
		}

		CFBamIndexColBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamIndexColBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a822" ) ) {
				editNext = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-update-next-", "Not " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, 0, "-delete-edit-next-", "Not " + Integer.toString(classCode));
			}
		}

					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						existing.getRequiredId() );
		CFBamBuffIndexColByUNameIdxKey keyUNameIdx = (CFBamBuffIndexColByUNameIdxKey)schema.getFactoryIndexCol().newByUNameIdxKey();
		keyUNameIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffIndexColByIndexIdxKey keyIndexIdx = (CFBamBuffIndexColByIndexIdxKey)schema.getFactoryIndexCol().newByIndexIdxKey();
		keyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamBuffIndexColByDefSchemaIdxKey keyDefSchemaIdx = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getFactoryIndexCol().newByDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffIndexColByColIdxKey keyColIdx = (CFBamBuffIndexColByColIdxKey)schema.getFactoryIndexCol().newByColIdxKey();
		keyColIdx.setRequiredColumnId( existing.getRequiredColumnId() );

		CFBamBuffIndexColByPrevIdxKey keyPrevIdx = (CFBamBuffIndexColByPrevIdxKey)schema.getFactoryIndexCol().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByNextIdxKey keyNextIdx = (CFBamBuffIndexColByNextIdxKey)schema.getFactoryIndexCol().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffIndexColByIdxPrevIdxKey keyIdxPrevIdx = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getFactoryIndexCol().newByIdxPrevIdxKey();
		keyIdxPrevIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyIdxPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffIndexColByIdxNextIdxKey keyIdxNextIdx = (CFBamBuffIndexColByIdxNextIdxKey)schema.getFactoryIndexCol().newByIdxNextIdxKey();
		keyIdxNextIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyIdxNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		if( schema.getTableRelationCol().readDerivedByFromColIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndexCol",
				"Lookup",
				"LookupFromCol",
				"RelationCol",
				pkey );
		}

		if( schema.getTableRelationCol().readDerivedByToColIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndexCol",
				"Lookup",
				"LookupToCol",
				"RelationCol",
				pkey );
		}

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffIndexCol > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIndexIdx.get( keyIndexIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByColIdx.get( keyColIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByIdxPrevIdx.get( keyIdxPrevIdx );
		subdict.remove( pkey );

		subdict = dictByIdxNextIdx.get( keyIdxNextIdx );
		subdict.remove( pkey );

	}
	public void deleteIndexColByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamIndexCol cur;
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId,
		String argName )
	{
		CFBamBuffIndexColByUNameIdxKey key = (CFBamBuffIndexColByUNameIdxKey)schema.getFactoryIndexCol().newByUNameIdxKey();
		key.setRequiredIndexId( argIndexId );
		key.setRequiredName( argName );
		deleteIndexColByUNameIdx( Authorization, key );
	}

	public void deleteIndexColByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByUNameIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIndexIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId )
	{
		CFBamBuffIndexColByIndexIdxKey key = (CFBamBuffIndexColByIndexIdxKey)schema.getFactoryIndexCol().newByIndexIdxKey();
		key.setRequiredIndexId( argIndexId );
		deleteIndexColByIndexIdx( Authorization, key );
	}

	public void deleteIndexColByIndexIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByIndexIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffIndexColByDefSchemaIdxKey key = (CFBamBuffIndexColByDefSchemaIdxKey)schema.getFactoryIndexCol().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexColByDefSchemaIdx( Authorization, key );
	}

	public void deleteIndexColByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByDefSchemaIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByColIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argColumnId )
	{
		CFBamBuffIndexColByColIdxKey key = (CFBamBuffIndexColByColIdxKey)schema.getFactoryIndexCol().newByColIdxKey();
		key.setRequiredColumnId( argColumnId );
		deleteIndexColByColIdx( Authorization, key );
	}

	public void deleteIndexColByColIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByColIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffIndexColByPrevIdxKey key = (CFBamBuffIndexColByPrevIdxKey)schema.getFactoryIndexCol().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteIndexColByPrevIdx( Authorization, key );
	}

	public void deleteIndexColByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByPrevIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffIndexColByNextIdxKey key = (CFBamBuffIndexColByNextIdxKey)schema.getFactoryIndexCol().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteIndexColByNextIdx( Authorization, key );
	}

	public void deleteIndexColByNextIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByNextIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIdxPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffIndexColByIdxPrevIdxKey key = (CFBamBuffIndexColByIdxPrevIdxKey)schema.getFactoryIndexCol().newByIdxPrevIdxKey();
		key.setRequiredIndexId( argIndexId );
		key.setOptionalPrevId( argPrevId );
		deleteIndexColByIdxPrevIdx( Authorization, key );
	}

	public void deleteIndexColByIdxPrevIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByIdxPrevIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIdxNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argIndexId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffIndexColByIdxNextIdxKey key = (CFBamBuffIndexColByIdxNextIdxKey)schema.getFactoryIndexCol().newByIdxNextIdxKey();
		key.setRequiredIndexId( argIndexId );
		key.setOptionalNextId( argNextId );
		deleteIndexColByIdxNextIdx( Authorization, key );
	}

	public void deleteIndexColByIdxNextIdx( ICFSecAuthorization Authorization,
		ICFBamIndexColByIdxNextIdxKey argKey )
	{
		ICFBamIndexCol cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamIndexCol> matchSet = new LinkedList<ICFBamIndexCol>();
		Iterator<ICFBamIndexCol> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamIndexCol> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}
}
