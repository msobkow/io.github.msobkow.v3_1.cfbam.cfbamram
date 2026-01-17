
// Description: Java 25 in-memory RAM DbIO implementation for Table.

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
 *	CFBamRamTableTable in-memory RAM DbIO implementation
 *	for Table.
 */
public class CFBamRamTableTable
	implements ICFBamTableTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamTableBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamTableBuff >();
	private Map< CFBamTableBySchemaDefIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >> dictBySchemaDefIdx
		= new HashMap< CFBamTableBySchemaDefIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >>();
	private Map< CFBamTableByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamTableByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >>();
	private Map< CFBamTableByUNameIdxKey,
			CFBamTableBuff > dictByUNameIdx
		= new HashMap< CFBamTableByUNameIdxKey,
			CFBamTableBuff >();
	private Map< CFBamTableBySchemaCdIdxKey,
			CFBamTableBuff > dictBySchemaCdIdx
		= new HashMap< CFBamTableBySchemaCdIdxKey,
			CFBamTableBuff >();
	private Map< CFBamTableByPrimaryIndexIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >> dictByPrimaryIndexIdx
		= new HashMap< CFBamTableByPrimaryIndexIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >>();
	private Map< CFBamTableByLookupIndexIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >> dictByLookupIndexIdx
		= new HashMap< CFBamTableByLookupIndexIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >>();
	private Map< CFBamTableByAltIndexIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >> dictByAltIndexIdx
		= new HashMap< CFBamTableByAltIndexIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >>();
	private Map< CFBamTableByQualTableIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >> dictByQualTableIdx
		= new HashMap< CFBamTableByQualTableIdxKey,
				Map< CFBamScopePKey,
					CFBamTableBuff >>();

	public CFBamRamTableTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createTable( CFSecAuthorization Authorization,
		CFBamTableBuff Buff )
	{
		final String S_ProcName = "createTable";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTableBySchemaDefIdxKey keySchemaDefIdx = schema.getFactoryTable().newSchemaDefIdxKey();
		keySchemaDefIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		CFBamTableByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryTable().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamTableByUNameIdxKey keyUNameIdx = schema.getFactoryTable().newUNameIdxKey();
		keyUNameIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamTableBySchemaCdIdxKey keySchemaCdIdx = schema.getFactoryTable().newSchemaCdIdxKey();
		keySchemaCdIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		keySchemaCdIdx.setRequiredTableClassCode( Buff.getRequiredTableClassCode() );

		CFBamTableByPrimaryIndexIdxKey keyPrimaryIndexIdx = schema.getFactoryTable().newPrimaryIndexIdxKey();
		keyPrimaryIndexIdx.setOptionalPrimaryIndexId( Buff.getOptionalPrimaryIndexId() );

		CFBamTableByLookupIndexIdxKey keyLookupIndexIdx = schema.getFactoryTable().newLookupIndexIdxKey();
		keyLookupIndexIdx.setOptionalLookupIndexId( Buff.getOptionalLookupIndexId() );

		CFBamTableByAltIndexIdxKey keyAltIndexIdx = schema.getFactoryTable().newAltIndexIdxKey();
		keyAltIndexIdx.setOptionalAltIndexId( Buff.getOptionalAltIndexId() );

		CFBamTableByQualTableIdxKey keyQualTableIdx = schema.getFactoryTable().newQualTableIdxKey();
		keyQualTableIdx.setOptionalQualifyingTableId( Buff.getOptionalQualifyingTableId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TableUNameIdx",
				keyUNameIdx );
		}

		if( dictBySchemaCdIdx.containsKey( keySchemaCdIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"TableSchemaCodeIdx",
				keySchemaCdIdx );
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamTableBuff > subdictSchemaDefIdx;
		if( dictBySchemaDefIdx.containsKey( keySchemaDefIdx ) ) {
			subdictSchemaDefIdx = dictBySchemaDefIdx.get( keySchemaDefIdx );
		}
		else {
			subdictSchemaDefIdx = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictBySchemaDefIdx.put( keySchemaDefIdx, subdictSchemaDefIdx );
		}
		subdictSchemaDefIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamTableBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		dictBySchemaCdIdx.put( keySchemaCdIdx, Buff );

		Map< CFBamScopePKey, CFBamTableBuff > subdictPrimaryIndexIdx;
		if( dictByPrimaryIndexIdx.containsKey( keyPrimaryIndexIdx ) ) {
			subdictPrimaryIndexIdx = dictByPrimaryIndexIdx.get( keyPrimaryIndexIdx );
		}
		else {
			subdictPrimaryIndexIdx = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByPrimaryIndexIdx.put( keyPrimaryIndexIdx, subdictPrimaryIndexIdx );
		}
		subdictPrimaryIndexIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamTableBuff > subdictLookupIndexIdx;
		if( dictByLookupIndexIdx.containsKey( keyLookupIndexIdx ) ) {
			subdictLookupIndexIdx = dictByLookupIndexIdx.get( keyLookupIndexIdx );
		}
		else {
			subdictLookupIndexIdx = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByLookupIndexIdx.put( keyLookupIndexIdx, subdictLookupIndexIdx );
		}
		subdictLookupIndexIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamTableBuff > subdictAltIndexIdx;
		if( dictByAltIndexIdx.containsKey( keyAltIndexIdx ) ) {
			subdictAltIndexIdx = dictByAltIndexIdx.get( keyAltIndexIdx );
		}
		else {
			subdictAltIndexIdx = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByAltIndexIdx.put( keyAltIndexIdx, subdictAltIndexIdx );
		}
		subdictAltIndexIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamTableBuff > subdictQualTableIdx;
		if( dictByQualTableIdx.containsKey( keyQualTableIdx ) ) {
			subdictQualTableIdx = dictByQualTableIdx.get( keyQualTableIdx );
		}
		else {
			subdictQualTableIdx = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByQualTableIdx.put( keyQualTableIdx, subdictQualTableIdx );
		}
		subdictQualTableIdx.put( pkey, Buff );

	}

	public CFBamTableBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamTable.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamTableBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamTable.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamTableBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamTable.readAllDerived";
		CFBamTableBuff[] retList = new CFBamTableBuff[ dictByPKey.values().size() ];
		Iterator< CFBamTableBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamTableBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamTableBuff ) ) {
					filteredList.add( (CFBamTableBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamTableBuff[0] ) );
		}
	}

	public CFBamTableBuff[] readDerivedBySchemaDefIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedBySchemaDefIdx";
		CFBamTableBySchemaDefIdxKey key = schema.getFactoryTable().newSchemaDefIdxKey();
		key.setRequiredSchemaDefId( SchemaDefId );

		CFBamTableBuff[] recArray;
		if( dictBySchemaDefIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamTableBuff > subdictSchemaDefIdx
				= dictBySchemaDefIdx.get( key );
			recArray = new CFBamTableBuff[ subdictSchemaDefIdx.size() ];
			Iterator< CFBamTableBuff > iter = subdictSchemaDefIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamTableBuff > subdictSchemaDefIdx
				= new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictBySchemaDefIdx.put( key, subdictSchemaDefIdx );
			recArray = new CFBamTableBuff[0];
		}
		return( recArray );
	}

	public CFBamTableBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByDefSchemaIdx";
		CFBamTableByDefSchemaIdxKey key = schema.getFactoryTable().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamTableBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamTableBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamTableBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamTableBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamTableBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamTableBuff[0];
		}
		return( recArray );
	}

	public CFBamTableBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByUNameIdx";
		CFBamTableByUNameIdxKey key = schema.getFactoryTable().newUNameIdxKey();
		key.setRequiredSchemaDefId( SchemaDefId );
		key.setRequiredName( Name );

		CFBamTableBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff readDerivedBySchemaCdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String TableClassCode )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedBySchemaCdIdx";
		CFBamTableBySchemaCdIdxKey key = schema.getFactoryTable().newSchemaCdIdxKey();
		key.setRequiredSchemaDefId( SchemaDefId );
		key.setRequiredTableClassCode( TableClassCode );

		CFBamTableBuff buff;
		if( dictBySchemaCdIdx.containsKey( key ) ) {
			buff = dictBySchemaCdIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff[] readDerivedByPrimaryIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrimaryIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByPrimaryIndexIdx";
		CFBamTableByPrimaryIndexIdxKey key = schema.getFactoryTable().newPrimaryIndexIdxKey();
		key.setOptionalPrimaryIndexId( PrimaryIndexId );

		CFBamTableBuff[] recArray;
		if( dictByPrimaryIndexIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamTableBuff > subdictPrimaryIndexIdx
				= dictByPrimaryIndexIdx.get( key );
			recArray = new CFBamTableBuff[ subdictPrimaryIndexIdx.size() ];
			Iterator< CFBamTableBuff > iter = subdictPrimaryIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamTableBuff > subdictPrimaryIndexIdx
				= new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByPrimaryIndexIdx.put( key, subdictPrimaryIndexIdx );
			recArray = new CFBamTableBuff[0];
		}
		return( recArray );
	}

	public CFBamTableBuff[] readDerivedByLookupIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 LookupIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByLookupIndexIdx";
		CFBamTableByLookupIndexIdxKey key = schema.getFactoryTable().newLookupIndexIdxKey();
		key.setOptionalLookupIndexId( LookupIndexId );

		CFBamTableBuff[] recArray;
		if( dictByLookupIndexIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamTableBuff > subdictLookupIndexIdx
				= dictByLookupIndexIdx.get( key );
			recArray = new CFBamTableBuff[ subdictLookupIndexIdx.size() ];
			Iterator< CFBamTableBuff > iter = subdictLookupIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamTableBuff > subdictLookupIndexIdx
				= new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByLookupIndexIdx.put( key, subdictLookupIndexIdx );
			recArray = new CFBamTableBuff[0];
		}
		return( recArray );
	}

	public CFBamTableBuff[] readDerivedByAltIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 AltIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByAltIndexIdx";
		CFBamTableByAltIndexIdxKey key = schema.getFactoryTable().newAltIndexIdxKey();
		key.setOptionalAltIndexId( AltIndexId );

		CFBamTableBuff[] recArray;
		if( dictByAltIndexIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamTableBuff > subdictAltIndexIdx
				= dictByAltIndexIdx.get( key );
			recArray = new CFBamTableBuff[ subdictAltIndexIdx.size() ];
			Iterator< CFBamTableBuff > iter = subdictAltIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamTableBuff > subdictAltIndexIdx
				= new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByAltIndexIdx.put( key, subdictAltIndexIdx );
			recArray = new CFBamTableBuff[0];
		}
		return( recArray );
	}

	public CFBamTableBuff[] readDerivedByQualTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 QualifyingTableId )
	{
		final String S_ProcName = "CFBamRamTable.readDerivedByQualTableIdx";
		CFBamTableByQualTableIdxKey key = schema.getFactoryTable().newQualTableIdxKey();
		key.setOptionalQualifyingTableId( QualifyingTableId );

		CFBamTableBuff[] recArray;
		if( dictByQualTableIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamTableBuff > subdictQualTableIdx
				= dictByQualTableIdx.get( key );
			recArray = new CFBamTableBuff[ subdictQualTableIdx.size() ];
			Iterator< CFBamTableBuff > iter = subdictQualTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamTableBuff > subdictQualTableIdx
				= new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByQualTableIdx.put( key, subdictQualTableIdx );
			recArray = new CFBamTableBuff[0];
		}
		return( recArray );
	}

	public CFBamTableBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamTableBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamTable.readBuff";
		CFBamTableBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a808" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamTableBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a808" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamTableBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamTable.readAllBuff";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamTableBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamTableBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTableBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff[] readBuffBySchemaDefIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId )
	{
		final String S_ProcName = "CFBamRamTable.readBuffBySchemaDefIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedBySchemaDefIdx( Authorization,
			SchemaDefId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamTable.readBuffByDefSchemaIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String Name )
	{
		final String S_ProcName = "CFBamRamTable.readBuffByUNameIdx() ";
		CFBamTableBuff buff = readDerivedByUNameIdx( Authorization,
			SchemaDefId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
			return( (CFBamTableBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTableBuff readBuffBySchemaCdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		String TableClassCode )
	{
		final String S_ProcName = "CFBamRamTable.readBuffBySchemaCdIdx() ";
		CFBamTableBuff buff = readDerivedBySchemaCdIdx( Authorization,
			SchemaDefId,
			TableClassCode );
		if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
			return( (CFBamTableBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamTableBuff[] readBuffByPrimaryIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrimaryIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readBuffByPrimaryIndexIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedByPrimaryIndexIdx( Authorization,
			PrimaryIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff[] readBuffByLookupIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 LookupIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readBuffByLookupIndexIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedByLookupIndexIdx( Authorization,
			LookupIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff[] readBuffByAltIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 AltIndexId )
	{
		final String S_ProcName = "CFBamRamTable.readBuffByAltIndexIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedByAltIndexIdx( Authorization,
			AltIndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	public CFBamTableBuff[] readBuffByQualTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 QualifyingTableId )
	{
		final String S_ProcName = "CFBamRamTable.readBuffByQualTableIdx() ";
		CFBamTableBuff buff;
		ArrayList<CFBamTableBuff> filteredList = new ArrayList<CFBamTableBuff>();
		CFBamTableBuff[] buffList = readDerivedByQualTableIdx( Authorization,
			QualifyingTableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a808" ) ) {
				filteredList.add( (CFBamTableBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamTableBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific Table buffer instances identified by the duplicate key SchemaDefIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	SchemaDefId	The Table key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableBuff[] pageBuffBySchemaDefIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaDefId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffBySchemaDefIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Table buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The Table key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Table buffer instances identified by the duplicate key PrimaryIndexIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PrimaryIndexId	The Table key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableBuff[] pageBuffByPrimaryIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrimaryIndexId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPrimaryIndexIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Table buffer instances identified by the duplicate key LookupIndexIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	LookupIndexId	The Table key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableBuff[] pageBuffByLookupIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 LookupIndexId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByLookupIndexIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Table buffer instances identified by the duplicate key AltIndexIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	AltIndexId	The Table key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableBuff[] pageBuffByAltIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 AltIndexId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByAltIndexIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Table buffer instances identified by the duplicate key QualTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	QualifyingTableId	The Table key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamTableBuff[] pageBuffByQualTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 QualifyingTableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByQualTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateTable( CFSecAuthorization Authorization,
		CFBamTableBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTableBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateTable",
				"Existing record not found",
				"Table",
				pkey );
		}
		CFBamTableBySchemaDefIdxKey existingKeySchemaDefIdx = schema.getFactoryTable().newSchemaDefIdxKey();
		existingKeySchemaDefIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamTableBySchemaDefIdxKey newKeySchemaDefIdx = schema.getFactoryTable().newSchemaDefIdxKey();
		newKeySchemaDefIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );

		CFBamTableByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryTable().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamTableByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryTable().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamTableByUNameIdxKey existingKeyUNameIdx = schema.getFactoryTable().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamTableByUNameIdxKey newKeyUNameIdx = schema.getFactoryTable().newUNameIdxKey();
		newKeyUNameIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamTableBySchemaCdIdxKey existingKeySchemaCdIdx = schema.getFactoryTable().newSchemaCdIdxKey();
		existingKeySchemaCdIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		existingKeySchemaCdIdx.setRequiredTableClassCode( existing.getRequiredTableClassCode() );

		CFBamTableBySchemaCdIdxKey newKeySchemaCdIdx = schema.getFactoryTable().newSchemaCdIdxKey();
		newKeySchemaCdIdx.setRequiredSchemaDefId( Buff.getRequiredSchemaDefId() );
		newKeySchemaCdIdx.setRequiredTableClassCode( Buff.getRequiredTableClassCode() );

		CFBamTableByPrimaryIndexIdxKey existingKeyPrimaryIndexIdx = schema.getFactoryTable().newPrimaryIndexIdxKey();
		existingKeyPrimaryIndexIdx.setOptionalPrimaryIndexId( existing.getOptionalPrimaryIndexId() );

		CFBamTableByPrimaryIndexIdxKey newKeyPrimaryIndexIdx = schema.getFactoryTable().newPrimaryIndexIdxKey();
		newKeyPrimaryIndexIdx.setOptionalPrimaryIndexId( Buff.getOptionalPrimaryIndexId() );

		CFBamTableByLookupIndexIdxKey existingKeyLookupIndexIdx = schema.getFactoryTable().newLookupIndexIdxKey();
		existingKeyLookupIndexIdx.setOptionalLookupIndexId( existing.getOptionalLookupIndexId() );

		CFBamTableByLookupIndexIdxKey newKeyLookupIndexIdx = schema.getFactoryTable().newLookupIndexIdxKey();
		newKeyLookupIndexIdx.setOptionalLookupIndexId( Buff.getOptionalLookupIndexId() );

		CFBamTableByAltIndexIdxKey existingKeyAltIndexIdx = schema.getFactoryTable().newAltIndexIdxKey();
		existingKeyAltIndexIdx.setOptionalAltIndexId( existing.getOptionalAltIndexId() );

		CFBamTableByAltIndexIdxKey newKeyAltIndexIdx = schema.getFactoryTable().newAltIndexIdxKey();
		newKeyAltIndexIdx.setOptionalAltIndexId( Buff.getOptionalAltIndexId() );

		CFBamTableByQualTableIdxKey existingKeyQualTableIdx = schema.getFactoryTable().newQualTableIdxKey();
		existingKeyQualTableIdx.setOptionalQualifyingTableId( existing.getOptionalQualifyingTableId() );

		CFBamTableByQualTableIdxKey newKeyQualTableIdx = schema.getFactoryTable().newQualTableIdxKey();
		newKeyQualTableIdx.setOptionalQualifyingTableId( Buff.getOptionalQualifyingTableId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTable",
					"TableUNameIdx",
					newKeyUNameIdx );
			}
		}

		if( ! existingKeySchemaCdIdx.equals( newKeySchemaCdIdx ) ) {
			if( dictBySchemaCdIdx.containsKey( newKeySchemaCdIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateTable",
					"TableSchemaCodeIdx",
					newKeySchemaCdIdx );
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
						"updateTable",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaDefId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateTable",
						"Container",
						"SchemaDef",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamTableBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaDefIdx.get( existingKeySchemaDefIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaDefIdx.containsKey( newKeySchemaDefIdx ) ) {
			subdict = dictBySchemaDefIdx.get( newKeySchemaDefIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictBySchemaDefIdx.put( newKeySchemaDefIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		dictBySchemaCdIdx.remove( existingKeySchemaCdIdx );
		dictBySchemaCdIdx.put( newKeySchemaCdIdx, Buff );

		subdict = dictByPrimaryIndexIdx.get( existingKeyPrimaryIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrimaryIndexIdx.containsKey( newKeyPrimaryIndexIdx ) ) {
			subdict = dictByPrimaryIndexIdx.get( newKeyPrimaryIndexIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByPrimaryIndexIdx.put( newKeyPrimaryIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByLookupIndexIdx.get( existingKeyLookupIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByLookupIndexIdx.containsKey( newKeyLookupIndexIdx ) ) {
			subdict = dictByLookupIndexIdx.get( newKeyLookupIndexIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByLookupIndexIdx.put( newKeyLookupIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByAltIndexIdx.get( existingKeyAltIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByAltIndexIdx.containsKey( newKeyAltIndexIdx ) ) {
			subdict = dictByAltIndexIdx.get( newKeyAltIndexIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByAltIndexIdx.put( newKeyAltIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByQualTableIdx.get( existingKeyQualTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByQualTableIdx.containsKey( newKeyQualTableIdx ) ) {
			subdict = dictByQualTableIdx.get( newKeyQualTableIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamTableBuff >();
			dictByQualTableIdx.put( newKeyQualTableIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteTable( CFSecAuthorization Authorization,
		CFBamTableBuff Buff )
	{
		final String S_ProcName = "CFBamRamTableTable.deleteTable() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamTableBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteTable",
				pkey );
		}
					{
						CFBamTableBuff editBuff = schema.getTableTable().readDerivedByIdIdx( Authorization,
						existing.getRequiredId() );
						editBuff.setOptionalPrimaryIndexId( null );
						classCode = editBuff.getClassCode();
						if( classCode.equals( "a808" ) ) {
							schema.getTableTable().updateTable( Authorization, editBuff );
						}
						else {
							new CFLibUnsupportedClassException( getClass(),
								S_ProcName,
								"Unrecognized ClassCode \"" + classCode + "\"" );
						}
					}
		CFBamTableBuff editSubobj = schema.getTableTable().readDerivedByIdIdx( Authorization,
			existing.getRequiredId() );
			editSubobj.setOptionalLookupIndexId( null );
			editSubobj.setOptionalAltIndexId( null );
			editSubobj.setOptionalPrimaryIndexId( null );
		classCode = editSubobj.getClassCode();
		if( classCode.equals( "a808" ) ) {
			schema.getTableTable().updateTable( Authorization, editSubobj );
		}
		else {
			new CFLibUnsupportedClassException( getClass(),
				S_ProcName,
				"Unrecognized ClassCode \"" + classCode + "\"" );
		}
		existing = editSubobj;
					schema.getTableServerMethod().deleteServerMethodByMethTableIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableDelTopDep().deleteDelTopDepByDelTopDepTblIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableClearTopDep().deleteClearTopDepByClrTopDepTblIdx( Authorization,
						existing.getRequiredId() );
					schema.getTableChain().deleteChainByChainTableIdx( Authorization,
						existing.getRequiredId() );
		CFBamRelationBuff buffDelTableRelationPopDep;
		CFBamRelationBuff arrDelTableRelationPopDep[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationPopDep = 0; idxDelTableRelationPopDep < arrDelTableRelationPopDep.length; idxDelTableRelationPopDep++ ) {
			buffDelTableRelationPopDep = arrDelTableRelationPopDep[idxDelTableRelationPopDep];
					schema.getTablePopTopDep().deletePopTopDepByContRelIdx( Authorization,
						buffDelTableRelationPopDep.getRequiredId() );
		}
		CFBamRelationBuff buffDelTableRelationCol;
		CFBamRelationBuff arrDelTableRelationCol[] = schema.getTableRelation().readDerivedByRelTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRelationCol = 0; idxDelTableRelationCol < arrDelTableRelationCol.length; idxDelTableRelationCol++ ) {
			buffDelTableRelationCol = arrDelTableRelationCol[idxDelTableRelationCol];
					schema.getTableRelationCol().deleteRelationColByRelationIdx( Authorization,
						buffDelTableRelationCol.getRequiredId() );
		}
					schema.getTableRelation().deleteRelationByRelTableIdx( Authorization,
						existing.getRequiredId() );
		CFBamIndexBuff buffDelTableIndexRefRelFmCol;
		CFBamIndexBuff arrDelTableIndexRefRelFmCol[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelFmCol = 0; idxDelTableIndexRefRelFmCol < arrDelTableIndexRefRelFmCol.length; idxDelTableIndexRefRelFmCol++ ) {
			buffDelTableIndexRefRelFmCol = arrDelTableIndexRefRelFmCol[idxDelTableIndexRefRelFmCol];
			CFBamIndexColBuff buffColumns;
			CFBamIndexColBuff arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				buffDelTableIndexRefRelFmCol.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = arrColumns[idxColumns];
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
		}
		CFBamIndexBuff buffDelTableIndexRefRelToCol;
		CFBamIndexBuff arrDelTableIndexRefRelToCol[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexRefRelToCol = 0; idxDelTableIndexRefRelToCol < arrDelTableIndexRefRelToCol.length; idxDelTableIndexRefRelToCol++ ) {
			buffDelTableIndexRefRelToCol = arrDelTableIndexRefRelToCol[idxDelTableIndexRefRelToCol];
			CFBamIndexColBuff buffColumns;
			CFBamIndexColBuff arrColumns[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				buffDelTableIndexRefRelToCol.getRequiredId() );
			for( int idxColumns = 0; idxColumns < arrColumns.length; idxColumns++ ) {
				buffColumns = arrColumns[idxColumns];
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffColumns.getRequiredId() );
			}
		}
		CFBamIndexBuff buffDelTableIndexCol;
		CFBamIndexBuff arrDelTableIndexCol[] = schema.getTableIndex().readDerivedByIdxTableIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableIndexCol = 0; idxDelTableIndexCol < arrDelTableIndexCol.length; idxDelTableIndexCol++ ) {
			buffDelTableIndexCol = arrDelTableIndexCol[idxDelTableIndexCol];
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						buffDelTableIndexCol.getRequiredId() );
		}
					schema.getTableIndex().deleteIndexByIdxTableIdx( Authorization,
						existing.getRequiredId() );
		CFBamValueBuff buffDelTableRefIndexColumns;
		CFBamValueBuff arrDelTableRefIndexColumns[] = schema.getTableValue().readDerivedByScopeIdx( Authorization,
			existing.getRequiredId() );
		for( int idxDelTableRefIndexColumns = 0; idxDelTableRefIndexColumns < arrDelTableRefIndexColumns.length; idxDelTableRefIndexColumns++ ) {
			buffDelTableRefIndexColumns = arrDelTableRefIndexColumns[idxDelTableRefIndexColumns];
					schema.getTableIndexCol().deleteIndexColByColIdx( Authorization,
						buffDelTableRefIndexColumns.getRequiredId() );
		}
					schema.getTableValue().deleteValueByScopeIdx( Authorization,
						existing.getRequiredId() );
		CFBamTableBySchemaDefIdxKey keySchemaDefIdx = schema.getFactoryTable().newSchemaDefIdxKey();
		keySchemaDefIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );

		CFBamTableByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryTable().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamTableByUNameIdxKey keyUNameIdx = schema.getFactoryTable().newUNameIdxKey();
		keyUNameIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamTableBySchemaCdIdxKey keySchemaCdIdx = schema.getFactoryTable().newSchemaCdIdxKey();
		keySchemaCdIdx.setRequiredSchemaDefId( existing.getRequiredSchemaDefId() );
		keySchemaCdIdx.setRequiredTableClassCode( existing.getRequiredTableClassCode() );

		CFBamTableByPrimaryIndexIdxKey keyPrimaryIndexIdx = schema.getFactoryTable().newPrimaryIndexIdxKey();
		keyPrimaryIndexIdx.setOptionalPrimaryIndexId( existing.getOptionalPrimaryIndexId() );

		CFBamTableByLookupIndexIdxKey keyLookupIndexIdx = schema.getFactoryTable().newLookupIndexIdxKey();
		keyLookupIndexIdx.setOptionalLookupIndexId( existing.getOptionalLookupIndexId() );

		CFBamTableByAltIndexIdxKey keyAltIndexIdx = schema.getFactoryTable().newAltIndexIdxKey();
		keyAltIndexIdx.setOptionalAltIndexId( existing.getOptionalAltIndexId() );

		CFBamTableByQualTableIdxKey keyQualTableIdx = schema.getFactoryTable().newQualTableIdxKey();
		keyQualTableIdx.setOptionalQualifyingTableId( existing.getOptionalQualifyingTableId() );

		// Validate reverse foreign keys

		if( schema.getTableRelation().readDerivedByToTblIdx( Authorization,
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteTable",
				"Lookup",
				"ToTable",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamTableBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaDefIdx.get( keySchemaDefIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		dictBySchemaCdIdx.remove( keySchemaCdIdx );

		subdict = dictByPrimaryIndexIdx.get( keyPrimaryIndexIdx );
		subdict.remove( pkey );

		subdict = dictByLookupIndexIdx.get( keyLookupIndexIdx );
		subdict.remove( pkey );

		subdict = dictByAltIndexIdx.get( keyAltIndexIdx );
		subdict.remove( pkey );

		subdict = dictByQualTableIdx.get( keyQualTableIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteTableBySchemaDefIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId )
	{
		CFBamTableBySchemaDefIdxKey key = schema.getFactoryTable().newSchemaDefIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		deleteTableBySchemaDefIdx( Authorization, key );
	}

	public void deleteTableBySchemaDefIdx( CFSecAuthorization Authorization,
		CFBamTableBySchemaDefIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamTableByDefSchemaIdxKey key = schema.getFactoryTable().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteTableByDefSchemaIdx( Authorization, key );
	}

	public void deleteTableByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamTableByDefSchemaIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId,
		String argName )
	{
		CFBamTableByUNameIdxKey key = schema.getFactoryTable().newUNameIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		key.setRequiredName( argName );
		deleteTableByUNameIdx( Authorization, key );
	}

	public void deleteTableByUNameIdx( CFSecAuthorization Authorization,
		CFBamTableByUNameIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableBySchemaCdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaDefId,
		String argTableClassCode )
	{
		CFBamTableBySchemaCdIdxKey key = schema.getFactoryTable().newSchemaCdIdxKey();
		key.setRequiredSchemaDefId( argSchemaDefId );
		key.setRequiredTableClassCode( argTableClassCode );
		deleteTableBySchemaCdIdx( Authorization, key );
	}

	public void deleteTableBySchemaCdIdx( CFSecAuthorization Authorization,
		CFBamTableBySchemaCdIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByPrimaryIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrimaryIndexId )
	{
		CFBamTableByPrimaryIndexIdxKey key = schema.getFactoryTable().newPrimaryIndexIdxKey();
		key.setOptionalPrimaryIndexId( argPrimaryIndexId );
		deleteTableByPrimaryIndexIdx( Authorization, key );
	}

	public void deleteTableByPrimaryIndexIdx( CFSecAuthorization Authorization,
		CFBamTableByPrimaryIndexIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrimaryIndexId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByLookupIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argLookupIndexId )
	{
		CFBamTableByLookupIndexIdxKey key = schema.getFactoryTable().newLookupIndexIdxKey();
		key.setOptionalLookupIndexId( argLookupIndexId );
		deleteTableByLookupIndexIdx( Authorization, key );
	}

	public void deleteTableByLookupIndexIdx( CFSecAuthorization Authorization,
		CFBamTableByLookupIndexIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalLookupIndexId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByAltIndexIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argAltIndexId )
	{
		CFBamTableByAltIndexIdxKey key = schema.getFactoryTable().newAltIndexIdxKey();
		key.setOptionalAltIndexId( argAltIndexId );
		deleteTableByAltIndexIdx( Authorization, key );
	}

	public void deleteTableByAltIndexIdx( CFSecAuthorization Authorization,
		CFBamTableByAltIndexIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalAltIndexId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByQualTableIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argQualifyingTableId )
	{
		CFBamTableByQualTableIdxKey key = schema.getFactoryTable().newQualTableIdxKey();
		key.setOptionalQualifyingTableId( argQualifyingTableId );
		deleteTableByQualTableIdx( Authorization, key );
	}

	public void deleteTableByQualTableIdx( CFSecAuthorization Authorization,
		CFBamTableByQualTableIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalQualifyingTableId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteTableByIdIdx( Authorization, key );
	}

	public void deleteTableByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamTableBuff cur;
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}

	public void deleteTableByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteTableByTenantIdx( Authorization, key );
	}

	public void deleteTableByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamTableBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamTableBuff> matchSet = new LinkedList<CFBamTableBuff>();
		Iterator<CFBamTableBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamTableBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableTable().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteTable( Authorization, cur );
		}
	}
}
