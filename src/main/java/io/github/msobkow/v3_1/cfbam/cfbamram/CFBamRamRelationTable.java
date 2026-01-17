
// Description: Java 25 in-memory RAM DbIO implementation for Relation.

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
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.dbutil.*;

import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfbam.cfbam.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;
import io.github.msobkow.v3_1.cfbam.cfbamobj.*;

/*
 *	CFBamRamRelationTable in-memory RAM DbIO implementation
 *	for Relation.
 */
public class CFBamRamRelationTable
	implements ICFBamRelationTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamRelationBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamRelationBuff >();
	private Map< CFBamRelationByUNameIdxKey,
			CFBamRelationBuff > dictByUNameIdx
		= new HashMap< CFBamRelationByUNameIdxKey,
			CFBamRelationBuff >();
	private Map< CFBamRelationByRelTableIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >> dictByRelTableIdx
		= new HashMap< CFBamRelationByRelTableIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >>();
	private Map< CFBamRelationByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamRelationByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >>();
	private Map< CFBamRelationByFromKeyIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >> dictByFromKeyIdx
		= new HashMap< CFBamRelationByFromKeyIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >>();
	private Map< CFBamRelationByToTblIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >> dictByToTblIdx
		= new HashMap< CFBamRelationByToTblIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >>();
	private Map< CFBamRelationByToKeyIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >> dictByToKeyIdx
		= new HashMap< CFBamRelationByToKeyIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >>();
	private Map< CFBamRelationByNarrowedIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >> dictByNarrowedIdx
		= new HashMap< CFBamRelationByNarrowedIdxKey,
				Map< CFBamScopePKey,
					CFBamRelationBuff >>();

	public CFBamRamRelationTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createRelation( CFSecAuthorization Authorization,
		CFBamRelationBuff Buff )
	{
		final String S_ProcName = "createRelation";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRelationByUNameIdxKey keyUNameIdx = schema.getFactoryRelation().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRelationByRelTableIdxKey keyRelTableIdx = schema.getFactoryRelation().newRelTableIdxKey();
		keyRelTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamRelationByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryRelation().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamRelationByFromKeyIdxKey keyFromKeyIdx = schema.getFactoryRelation().newFromKeyIdxKey();
		keyFromKeyIdx.setRequiredFromIndexId( Buff.getRequiredFromIndexId() );

		CFBamRelationByToTblIdxKey keyToTblIdx = schema.getFactoryRelation().newToTblIdxKey();
		keyToTblIdx.setRequiredToTableId( Buff.getRequiredToTableId() );

		CFBamRelationByToKeyIdxKey keyToKeyIdx = schema.getFactoryRelation().newToKeyIdxKey();
		keyToKeyIdx.setRequiredToIndexId( Buff.getRequiredToIndexId() );

		CFBamRelationByNarrowedIdxKey keyNarrowedIdx = schema.getFactoryRelation().newNarrowedIdxKey();
		keyNarrowedIdx.setOptionalNarrowedId( Buff.getOptionalNarrowedId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"RelationUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"FromTable",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"FromIndex",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"ToTable",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"ToIndex",
						"Index",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamRelationBuff > subdictRelTableIdx;
		if( dictByRelTableIdx.containsKey( keyRelTableIdx ) ) {
			subdictRelTableIdx = dictByRelTableIdx.get( keyRelTableIdx );
		}
		else {
			subdictRelTableIdx = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByRelTableIdx.put( keyRelTableIdx, subdictRelTableIdx );
		}
		subdictRelTableIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamRelationBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamRelationBuff > subdictFromKeyIdx;
		if( dictByFromKeyIdx.containsKey( keyFromKeyIdx ) ) {
			subdictFromKeyIdx = dictByFromKeyIdx.get( keyFromKeyIdx );
		}
		else {
			subdictFromKeyIdx = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByFromKeyIdx.put( keyFromKeyIdx, subdictFromKeyIdx );
		}
		subdictFromKeyIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamRelationBuff > subdictToTblIdx;
		if( dictByToTblIdx.containsKey( keyToTblIdx ) ) {
			subdictToTblIdx = dictByToTblIdx.get( keyToTblIdx );
		}
		else {
			subdictToTblIdx = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByToTblIdx.put( keyToTblIdx, subdictToTblIdx );
		}
		subdictToTblIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamRelationBuff > subdictToKeyIdx;
		if( dictByToKeyIdx.containsKey( keyToKeyIdx ) ) {
			subdictToKeyIdx = dictByToKeyIdx.get( keyToKeyIdx );
		}
		else {
			subdictToKeyIdx = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByToKeyIdx.put( keyToKeyIdx, subdictToKeyIdx );
		}
		subdictToKeyIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamRelationBuff > subdictNarrowedIdx;
		if( dictByNarrowedIdx.containsKey( keyNarrowedIdx ) ) {
			subdictNarrowedIdx = dictByNarrowedIdx.get( keyNarrowedIdx );
		}
		else {
			subdictNarrowedIdx = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByNarrowedIdx.put( keyNarrowedIdx, subdictNarrowedIdx );
		}
		subdictNarrowedIdx.put( pkey, Buff );

	}

	public CFBamRelationBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamRelation.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamRelationBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamRelation.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamRelationBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamRelation.readAllDerived";
		CFBamRelationBuff[] retList = new CFBamRelationBuff[ dictByPKey.values().size() ];
		Iterator< CFBamRelationBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamRelationBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamScopeBuff buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamScopeBuff buff;
			ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamRelationBuff ) ) {
					filteredList.add( (CFBamRelationBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamRelationBuff[0] ) );
		}
	}

	public CFBamRelationBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByUNameIdx";
		CFBamRelationByUNameIdxKey key = schema.getFactoryRelation().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamRelationBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationBuff[] readDerivedByRelTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByRelTableIdx";
		CFBamRelationByRelTableIdxKey key = schema.getFactoryRelation().newRelTableIdxKey();
		key.setRequiredTableId( TableId );

		CFBamRelationBuff[] recArray;
		if( dictByRelTableIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictRelTableIdx
				= dictByRelTableIdx.get( key );
			recArray = new CFBamRelationBuff[ subdictRelTableIdx.size() ];
			Iterator< CFBamRelationBuff > iter = subdictRelTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictRelTableIdx
				= new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByRelTableIdx.put( key, subdictRelTableIdx );
			recArray = new CFBamRelationBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByDefSchemaIdx";
		CFBamRelationByDefSchemaIdxKey key = schema.getFactoryRelation().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamRelationBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamRelationBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamRelationBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamRelationBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationBuff[] readDerivedByFromKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByFromKeyIdx";
		CFBamRelationByFromKeyIdxKey key = schema.getFactoryRelation().newFromKeyIdxKey();
		key.setRequiredFromIndexId( FromIndexId );

		CFBamRelationBuff[] recArray;
		if( dictByFromKeyIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictFromKeyIdx
				= dictByFromKeyIdx.get( key );
			recArray = new CFBamRelationBuff[ subdictFromKeyIdx.size() ];
			Iterator< CFBamRelationBuff > iter = subdictFromKeyIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictFromKeyIdx
				= new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByFromKeyIdx.put( key, subdictFromKeyIdx );
			recArray = new CFBamRelationBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationBuff[] readDerivedByToTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToTableId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByToTblIdx";
		CFBamRelationByToTblIdxKey key = schema.getFactoryRelation().newToTblIdxKey();
		key.setRequiredToTableId( ToTableId );

		CFBamRelationBuff[] recArray;
		if( dictByToTblIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictToTblIdx
				= dictByToTblIdx.get( key );
			recArray = new CFBamRelationBuff[ subdictToTblIdx.size() ];
			Iterator< CFBamRelationBuff > iter = subdictToTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictToTblIdx
				= new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByToTblIdx.put( key, subdictToTblIdx );
			recArray = new CFBamRelationBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationBuff[] readDerivedByToKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByToKeyIdx";
		CFBamRelationByToKeyIdxKey key = schema.getFactoryRelation().newToKeyIdxKey();
		key.setRequiredToIndexId( ToIndexId );

		CFBamRelationBuff[] recArray;
		if( dictByToKeyIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictToKeyIdx
				= dictByToKeyIdx.get( key );
			recArray = new CFBamRelationBuff[ subdictToKeyIdx.size() ];
			Iterator< CFBamRelationBuff > iter = subdictToKeyIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictToKeyIdx
				= new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByToKeyIdx.put( key, subdictToKeyIdx );
			recArray = new CFBamRelationBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationBuff[] readDerivedByNarrowedIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NarrowedId )
	{
		final String S_ProcName = "CFBamRamRelation.readDerivedByNarrowedIdx";
		CFBamRelationByNarrowedIdxKey key = schema.getFactoryRelation().newNarrowedIdxKey();
		key.setOptionalNarrowedId( NarrowedId );

		CFBamRelationBuff[] recArray;
		if( dictByNarrowedIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictNarrowedIdx
				= dictByNarrowedIdx.get( key );
			recArray = new CFBamRelationBuff[ subdictNarrowedIdx.size() ];
			Iterator< CFBamRelationBuff > iter = subdictNarrowedIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamRelationBuff > subdictNarrowedIdx
				= new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByNarrowedIdx.put( key, subdictNarrowedIdx );
			recArray = new CFBamRelationBuff[0];
		}
		return( recArray );
	}

	public CFBamRelationBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamRelationBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamRelation.readBuff";
		CFBamRelationBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a835" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamRelationBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a835" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamRelationBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamRelation.readAllBuff";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamRelationBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamRelationBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRelationBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByUNameIdx() ";
		CFBamRelationBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
			return( (CFBamRelationBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamRelationBuff[] readBuffByRelTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByRelTableIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByRelTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByDefSchemaIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff[] readBuffByFromKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByFromKeyIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByFromKeyIdx( Authorization,
			FromIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff[] readBuffByToTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToTableId )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByToTblIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByToTblIdx( Authorization,
			ToTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff[] readBuffByToKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToIndexId )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByToKeyIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByToKeyIdx( Authorization,
			ToIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	public CFBamRelationBuff[] readBuffByNarrowedIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NarrowedId )
	{
		final String S_ProcName = "CFBamRamRelation.readBuffByNarrowedIdx() ";
		CFBamRelationBuff buff;
		ArrayList<CFBamRelationBuff> filteredList = new ArrayList<CFBamRelationBuff>();
		CFBamRelationBuff[] buffList = readDerivedByNarrowedIdx( Authorization,
			NarrowedId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a835" ) ) {
				filteredList.add( (CFBamRelationBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamRelationBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific Relation buffer instances identified by the duplicate key RelTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The Relation key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamRelationBuff[] pageBuffByRelTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRelTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Relation buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The Relation key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamRelationBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Relation buffer instances identified by the duplicate key FromKeyIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	FromIndexId	The Relation key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamRelationBuff[] pageBuffByFromKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 FromIndexId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByFromKeyIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Relation buffer instances identified by the duplicate key ToTblIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ToTableId	The Relation key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamRelationBuff[] pageBuffByToTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToTableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByToTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Relation buffer instances identified by the duplicate key ToKeyIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	ToIndexId	The Relation key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamRelationBuff[] pageBuffByToKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ToIndexId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByToKeyIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Relation buffer instances identified by the duplicate key NarrowedIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	NarrowedId	The Relation key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamRelationBuff[] pageBuffByNarrowedIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NarrowedId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByNarrowedIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateRelation( CFSecAuthorization Authorization,
		CFBamRelationBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRelationBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateRelation",
				"Existing record not found",
				"Relation",
				pkey );
		}
		CFBamRelationByUNameIdxKey existingKeyUNameIdx = schema.getFactoryRelation().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamRelationByUNameIdxKey newKeyUNameIdx = schema.getFactoryRelation().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamRelationByRelTableIdxKey existingKeyRelTableIdx = schema.getFactoryRelation().newRelTableIdxKey();
		existingKeyRelTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamRelationByRelTableIdxKey newKeyRelTableIdx = schema.getFactoryRelation().newRelTableIdxKey();
		newKeyRelTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamRelationByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryRelation().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamRelationByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryRelation().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamRelationByFromKeyIdxKey existingKeyFromKeyIdx = schema.getFactoryRelation().newFromKeyIdxKey();
		existingKeyFromKeyIdx.setRequiredFromIndexId( existing.getRequiredFromIndexId() );

		CFBamRelationByFromKeyIdxKey newKeyFromKeyIdx = schema.getFactoryRelation().newFromKeyIdxKey();
		newKeyFromKeyIdx.setRequiredFromIndexId( Buff.getRequiredFromIndexId() );

		CFBamRelationByToTblIdxKey existingKeyToTblIdx = schema.getFactoryRelation().newToTblIdxKey();
		existingKeyToTblIdx.setRequiredToTableId( existing.getRequiredToTableId() );

		CFBamRelationByToTblIdxKey newKeyToTblIdx = schema.getFactoryRelation().newToTblIdxKey();
		newKeyToTblIdx.setRequiredToTableId( Buff.getRequiredToTableId() );

		CFBamRelationByToKeyIdxKey existingKeyToKeyIdx = schema.getFactoryRelation().newToKeyIdxKey();
		existingKeyToKeyIdx.setRequiredToIndexId( existing.getRequiredToIndexId() );

		CFBamRelationByToKeyIdxKey newKeyToKeyIdx = schema.getFactoryRelation().newToKeyIdxKey();
		newKeyToKeyIdx.setRequiredToIndexId( Buff.getRequiredToIndexId() );

		CFBamRelationByNarrowedIdxKey existingKeyNarrowedIdx = schema.getFactoryRelation().newNarrowedIdxKey();
		existingKeyNarrowedIdx.setOptionalNarrowedId( existing.getOptionalNarrowedId() );

		CFBamRelationByNarrowedIdxKey newKeyNarrowedIdx = schema.getFactoryRelation().newNarrowedIdxKey();
		newKeyNarrowedIdx.setOptionalNarrowedId( Buff.getOptionalNarrowedId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateRelation",
					"RelationUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Container",
						"FromTable",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredFromIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Lookup",
						"FromIndex",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Lookup",
						"ToTable",
						"Table",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredToIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateRelation",
						"Lookup",
						"ToIndex",
						"Index",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamRelationBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRelTableIdx.get( existingKeyRelTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRelTableIdx.containsKey( newKeyRelTableIdx ) ) {
			subdict = dictByRelTableIdx.get( newKeyRelTableIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByRelTableIdx.put( newKeyRelTableIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByFromKeyIdx.get( existingKeyFromKeyIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByFromKeyIdx.containsKey( newKeyFromKeyIdx ) ) {
			subdict = dictByFromKeyIdx.get( newKeyFromKeyIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByFromKeyIdx.put( newKeyFromKeyIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByToTblIdx.get( existingKeyToTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByToTblIdx.containsKey( newKeyToTblIdx ) ) {
			subdict = dictByToTblIdx.get( newKeyToTblIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByToTblIdx.put( newKeyToTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByToKeyIdx.get( existingKeyToKeyIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByToKeyIdx.containsKey( newKeyToKeyIdx ) ) {
			subdict = dictByToKeyIdx.get( newKeyToKeyIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByToKeyIdx.put( newKeyToKeyIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNarrowedIdx.get( existingKeyNarrowedIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNarrowedIdx.containsKey( newKeyNarrowedIdx ) ) {
			subdict = dictByNarrowedIdx.get( newKeyNarrowedIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamRelationBuff >();
			dictByNarrowedIdx.put( newKeyNarrowedIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteRelation( CFSecAuthorization Authorization,
		CFBamRelationBuff Buff )
	{
		final String S_ProcName = "CFBamRamRelationTable.deleteRelation() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamRelationBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteRelation",
				pkey );
		}
					schema.getTablePopTopDep().deletePopTopDepByContRelIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						existing.getRequiredId() );
		CFBamRelationByUNameIdxKey keyUNameIdx = schema.getFactoryRelation().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamRelationByRelTableIdxKey keyRelTableIdx = schema.getFactoryRelation().newRelTableIdxKey();
		keyRelTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamRelationByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryRelation().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamRelationByFromKeyIdxKey keyFromKeyIdx = schema.getFactoryRelation().newFromKeyIdxKey();
		keyFromKeyIdx.setRequiredFromIndexId( existing.getRequiredFromIndexId() );

		CFBamRelationByToTblIdxKey keyToTblIdx = schema.getFactoryRelation().newToTblIdxKey();
		keyToTblIdx.setRequiredToTableId( existing.getRequiredToTableId() );

		CFBamRelationByToKeyIdxKey keyToKeyIdx = schema.getFactoryRelation().newToKeyIdxKey();
		keyToKeyIdx.setRequiredToIndexId( existing.getRequiredToIndexId() );

		CFBamRelationByNarrowedIdxKey keyNarrowedIdx = schema.getFactoryRelation().newNarrowedIdxKey();
		keyNarrowedIdx.setOptionalNarrowedId( existing.getOptionalNarrowedId() );

		// Validate reverse foreign keys

		if( schema.getTableChain().readDerivedByPrevRelIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"PrevRelation",
				"Chain",
				pkey );
		}

		if( schema.getTableChain().readDerivedByNextRelIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"NextRelation",
				"Chain",
				pkey );
		}

		if( schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Relation",
				"ClearDep",
				pkey );
		}

		if( schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Relation",
				"DelDep",
				pkey );
		}

		if( schema.getTablePopDep().readDerivedByRelationIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteRelation",
				"Lookup",
				"Relation",
				"PopDep",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamRelationBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRelTableIdx.get( keyRelTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByFromKeyIdx.get( keyFromKeyIdx );
		subdict.remove( pkey );

		subdict = dictByToTblIdx.get( keyToTblIdx );
		subdict.remove( pkey );

		subdict = dictByToKeyIdx.get( keyToKeyIdx );
		subdict.remove( pkey );

		subdict = dictByNarrowedIdx.get( keyNarrowedIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteRelationByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamRelationByUNameIdxKey key = schema.getFactoryRelation().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteRelationByUNameIdx( Authorization, key );
	}

	public void deleteRelationByUNameIdx( CFSecAuthorization Authorization,
		CFBamRelationByUNameIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByRelTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamRelationByRelTableIdxKey key = schema.getFactoryRelation().newRelTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteRelationByRelTableIdx( Authorization, key );
	}

	public void deleteRelationByRelTableIdx( CFSecAuthorization Authorization,
		CFBamRelationByRelTableIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamRelationByDefSchemaIdxKey key = schema.getFactoryRelation().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteRelationByDefSchemaIdx( Authorization, key );
	}

	public void deleteRelationByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamRelationByDefSchemaIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByFromKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argFromIndexId )
	{
		CFBamRelationByFromKeyIdxKey key = schema.getFactoryRelation().newFromKeyIdxKey();
		key.setRequiredFromIndexId( argFromIndexId );
		deleteRelationByFromKeyIdx( Authorization, key );
	}

	public void deleteRelationByFromKeyIdx( CFSecAuthorization Authorization,
		CFBamRelationByFromKeyIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByToTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToTableId )
	{
		CFBamRelationByToTblIdxKey key = schema.getFactoryRelation().newToTblIdxKey();
		key.setRequiredToTableId( argToTableId );
		deleteRelationByToTblIdx( Authorization, key );
	}

	public void deleteRelationByToTblIdx( CFSecAuthorization Authorization,
		CFBamRelationByToTblIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByToKeyIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argToIndexId )
	{
		CFBamRelationByToKeyIdxKey key = schema.getFactoryRelation().newToKeyIdxKey();
		key.setRequiredToIndexId( argToIndexId );
		deleteRelationByToKeyIdx( Authorization, key );
	}

	public void deleteRelationByToKeyIdx( CFSecAuthorization Authorization,
		CFBamRelationByToKeyIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByNarrowedIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNarrowedId )
	{
		CFBamRelationByNarrowedIdxKey key = schema.getFactoryRelation().newNarrowedIdxKey();
		key.setOptionalNarrowedId( argNarrowedId );
		deleteRelationByNarrowedIdx( Authorization, key );
	}

	public void deleteRelationByNarrowedIdx( CFSecAuthorization Authorization,
		CFBamRelationByNarrowedIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNarrowedId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteRelationByIdIdx( Authorization, key );
	}

	public void deleteRelationByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamRelationBuff cur;
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}

	public void deleteRelationByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteRelationByTenantIdx( Authorization, key );
	}

	public void deleteRelationByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamRelationBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamRelationBuff> matchSet = new LinkedList<CFBamRelationBuff>();
		Iterator<CFBamRelationBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamRelationBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableRelation().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteRelation( Authorization, cur );
		}
	}
}
