
// Description: Java 25 in-memory RAM DbIO implementation for SchemaRef.

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
 *	CFBamRamSchemaRefTable in-memory RAM DbIO implementation
 *	for SchemaRef.
 */
public class CFBamRamSchemaRefTable
	implements ICFBamSchemaRefTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamSchemaRefBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamSchemaRefBuff >();
	private Map< CFBamSchemaRefBySchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictBySchemaIdx
		= new HashMap< CFBamSchemaRefBySchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();
	private Map< CFBamSchemaRefByUNameIdxKey,
			CFBamSchemaRefBuff > dictByUNameIdx
		= new HashMap< CFBamSchemaRefByUNameIdxKey,
			CFBamSchemaRefBuff >();
	private Map< CFBamSchemaRefByRefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictByRefSchemaIdx
		= new HashMap< CFBamSchemaRefByRefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();
	private Map< CFBamSchemaRefByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictByPrevIdx
		= new HashMap< CFBamSchemaRefByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();
	private Map< CFBamSchemaRefByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictByNextIdx
		= new HashMap< CFBamSchemaRefByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();

	public CFBamRamSchemaRefTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createSchemaRef( CFSecAuthorization Authorization,
		CFBamSchemaRefBuff Buff )
	{
		final String S_ProcName = "createSchemaRef";
		CFBamSchemaRefBuff tail = null;
		if( Buff.getClassCode().equals( "a804" ) ) {
			CFBamSchemaRefBuff[] siblings = schema.getTableSchemaRef().readDerivedBySchemaIdx( Authorization,
				Buff.getRequiredSchemaId() );
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
		}
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRefBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		keySchemaIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );

		CFBamSchemaRefByUNameIdxKey keyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		keyUNameIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamSchemaRefByRefSchemaIdxKey keyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		keyRefSchemaIdx.setOptionalRefSchemaId( Buff.getOptionalRefSchemaId() );

		CFBamSchemaRefByPrevIdxKey keyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamSchemaRefByNextIdxKey keyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaRefUNameIdx",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredSchemaId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictRefSchemaIdx;
		if( dictByRefSchemaIdx.containsKey( keyRefSchemaIdx ) ) {
			subdictRefSchemaIdx = dictByRefSchemaIdx.get( keyRefSchemaIdx );
		}
		else {
			subdictRefSchemaIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByRefSchemaIdx.put( keyRefSchemaIdx, subdictRefSchemaIdx );
		}
		subdictRefSchemaIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			String tailClassCode = tail.getClassCode();
			if( tailClassCode.equals( "a804" ) ) {
				CFBamSchemaRefBuff tailEdit = schema.getFactorySchemaRef().newBuff();
				tailEdit.set( (CFBamSchemaRefBuff)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableSchemaRef().updateSchemaRef( Authorization, tailEdit );
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode " + tailClassCode );
			}
		}
	}

	public CFBamSchemaRefBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaRefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaRefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaRef.readAllDerived";
		CFBamSchemaRefBuff[] retList = new CFBamSchemaRefBuff[ dictByPKey.values().size() ];
		Iterator< CFBamSchemaRefBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamSchemaRefBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaRefBuff ) ) {
					filteredList.add( (CFBamSchemaRefBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
		}
	}

	public CFBamSchemaRefBuff[] readDerivedBySchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedBySchemaIdx";
		CFBamSchemaRefBySchemaIdxKey key = schema.getFactorySchemaRef().newSchemaIdxKey();
		key.setRequiredSchemaId( SchemaId );

		CFBamSchemaRefBuff[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictSchemaIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByUNameIdx";
		CFBamSchemaRefByUNameIdxKey key = schema.getFactorySchemaRef().newUNameIdxKey();
		key.setRequiredSchemaId( SchemaId );
		key.setRequiredName( Name );

		CFBamSchemaRefBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff[] readDerivedByRefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RefSchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByRefSchemaIdx";
		CFBamSchemaRefByRefSchemaIdxKey key = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		key.setOptionalRefSchemaId( RefSchemaId );

		CFBamSchemaRefBuff[] recArray;
		if( dictByRefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictRefSchemaIdx
				= dictByRefSchemaIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictRefSchemaIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictRefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictRefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByRefSchemaIdx.put( key, subdictRefSchemaIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByPrevIdx";
		CFBamSchemaRefByPrevIdxKey key = schema.getFactorySchemaRef().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		CFBamSchemaRefBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictPrevIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByNextIdx";
		CFBamSchemaRefByNextIdxKey key = schema.getFactorySchemaRef().newNextIdxKey();
		key.setOptionalNextId( NextId );

		CFBamSchemaRefBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictNextIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictNextIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamSchemaRefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuff";
		CFBamSchemaRefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a804" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamSchemaRefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a804" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readAllBuff";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamSchemaRefBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamSchemaRefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRefBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff[] readBuffBySchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffBySchemaIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedBySchemaIdx( Authorization,
			SchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByUNameIdx() ";
		CFBamSchemaRefBuff buff = readDerivedByUNameIdx( Authorization,
			SchemaId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
			return( (CFBamSchemaRefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRefBuff[] readBuffByRefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RefSchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByRefSchemaIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByRefSchemaIdx( Authorization,
			RefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByPrevIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByNextIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key SchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	SchemaId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffBySchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 SchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffBySchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key RefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RefSchemaId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffByRefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByRefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key PrevIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PrevId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPrevIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key NextIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	NextId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByNextIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamSchemaRefBuff moveBuffUp( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamSchemaRefBuff grandprev = null;
		CFBamSchemaRefBuff prev = null;
		CFBamSchemaRefBuff cur = null;
		CFBamSchemaRefBuff next = null;

		cur = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamSchemaRefBuff)cur );
		}

		prev = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamSchemaRefBuff newInstance;
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editCur = newInstance;
		editCur.set( cur );

		CFBamSchemaRefBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamSchemaRefBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
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
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamSchemaRefBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamSchemaRefBuff moveBuffDown( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamSchemaRefBuff prev = null;
		CFBamSchemaRefBuff cur = null;
		CFBamSchemaRefBuff next = null;
		CFBamSchemaRefBuff grandnext = null;

		cur = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamSchemaRefBuff)cur );
		}

		next = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamSchemaRefBuff newInstance;
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editNext = newInstance;
		editNext.set( next );

		CFBamSchemaRefBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamSchemaRefBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
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
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamSchemaRefBuff)editCur );
	}

	public void updateSchemaRef( CFSecAuthorization Authorization,
		CFBamSchemaRefBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaRef",
				"Existing record not found",
				"SchemaRef",
				pkey );
		}
		CFBamSchemaRefBySchemaIdxKey existingKeySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		existingKeySchemaIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );

		CFBamSchemaRefBySchemaIdxKey newKeySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		newKeySchemaIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );

		CFBamSchemaRefByUNameIdxKey existingKeyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamSchemaRefByUNameIdxKey newKeyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		newKeyUNameIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamSchemaRefByRefSchemaIdxKey existingKeyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		existingKeyRefSchemaIdx.setOptionalRefSchemaId( existing.getOptionalRefSchemaId() );

		CFBamSchemaRefByRefSchemaIdxKey newKeyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		newKeyRefSchemaIdx.setOptionalRefSchemaId( Buff.getOptionalRefSchemaId() );

		CFBamSchemaRefByPrevIdxKey existingKeyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamSchemaRefByPrevIdxKey newKeyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamSchemaRefByNextIdxKey existingKeyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamSchemaRefByNextIdxKey newKeyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaRef",
					"SchemaRefUNameIdx",
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
						"updateSchemaRef",
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
						Buff.getRequiredSchemaId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaRef",
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaIdx.get( existingKeySchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaIdx.containsKey( newKeySchemaIdx ) ) {
			subdict = dictBySchemaIdx.get( newKeySchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRefSchemaIdx.get( existingKeyRefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRefSchemaIdx.containsKey( newKeyRefSchemaIdx ) ) {
			subdict = dictByRefSchemaIdx.get( newKeyRefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByRefSchemaIdx.put( newKeyRefSchemaIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
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
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteSchemaRef( CFSecAuthorization Authorization,
		CFBamSchemaRefBuff Buff )
	{
		final String S_ProcName = "CFBamRamSchemaRefTable.deleteSchemaRef() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaRef",
				pkey );
		}
		CFLibDbKeyHash256 varSchemaId = existing.getRequiredSchemaId();
		CFBamSchemaDefBuff container = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
			varSchemaId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamSchemaRefBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamSchemaRefBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				editPrev = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamSchemaRefBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamSchemaRefBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a804" ) ) {
				editNext = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamSchemaRefBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		keySchemaIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );

		CFBamSchemaRefByUNameIdxKey keyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		keyUNameIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamSchemaRefByRefSchemaIdxKey keyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		keyRefSchemaIdx.setOptionalRefSchemaId( existing.getOptionalRefSchemaId() );

		CFBamSchemaRefByPrevIdxKey keyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamSchemaRefByNextIdxKey keyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRefSchemaIdx.get( keyRefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteSchemaRefBySchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaId )
	{
		CFBamSchemaRefBySchemaIdxKey key = schema.getFactorySchemaRef().newSchemaIdxKey();
		key.setRequiredSchemaId( argSchemaId );
		deleteSchemaRefBySchemaIdx( Authorization, key );
	}

	public void deleteSchemaRefBySchemaIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefBySchemaIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argSchemaId,
		String argName )
	{
		CFBamSchemaRefByUNameIdxKey key = schema.getFactorySchemaRef().newUNameIdxKey();
		key.setRequiredSchemaId( argSchemaId );
		key.setRequiredName( argName );
		deleteSchemaRefByUNameIdx( Authorization, key );
	}

	public void deleteSchemaRefByUNameIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByUNameIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByRefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRefSchemaId )
	{
		CFBamSchemaRefByRefSchemaIdxKey key = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		key.setOptionalRefSchemaId( argRefSchemaId );
		deleteSchemaRefByRefSchemaIdx( Authorization, key );
	}

	public void deleteSchemaRefByRefSchemaIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByRefSchemaIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamSchemaRefByPrevIdxKey key = schema.getFactorySchemaRef().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteSchemaRefByPrevIdx( Authorization, key );
	}

	public void deleteSchemaRefByPrevIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByPrevIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamSchemaRefByNextIdxKey key = schema.getFactorySchemaRef().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteSchemaRefByNextIdx( Authorization, key );
	}

	public void deleteSchemaRefByNextIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByNextIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteSchemaRefByIdIdx( Authorization, key );
	}

	public void deleteSchemaRefByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamSchemaRefBuff cur;
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaRefByTenantIdx( Authorization, key );
	}

	public void deleteSchemaRefByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}
}
