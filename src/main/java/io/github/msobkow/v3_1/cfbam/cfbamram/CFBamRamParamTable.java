
// Description: Java 25 in-memory RAM DbIO implementation for Param.

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
 *	CFBamRamParamTable in-memory RAM DbIO implementation
 *	for Param.
 */
public class CFBamRamParamTable
	implements ICFBamParamTable
{
	private ICFBamSchema schema;
	private Map< CFBamParamPKey,
				CFBamParamBuff > dictByPKey
		= new HashMap< CFBamParamPKey,
				CFBamParamBuff >();
	private Map< CFBamParamByUNameIdxKey,
			CFBamParamBuff > dictByUNameIdx
		= new HashMap< CFBamParamByUNameIdxKey,
			CFBamParamBuff >();
	private Map< CFBamParamByServerMethodIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByServerMethodIdx
		= new HashMap< CFBamParamByServerMethodIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByDefSchemaIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamParamByDefSchemaIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByServerTypeIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByServerTypeIdx
		= new HashMap< CFBamParamByServerTypeIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByPrevIdx
		= new HashMap< CFBamParamByPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByNextIdx
		= new HashMap< CFBamParamByNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByContPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByContPrevIdx
		= new HashMap< CFBamParamByContPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByContNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByContNextIdx
		= new HashMap< CFBamParamByContNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();

	public CFBamRamParamTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createParam( CFSecAuthorization Authorization,
		CFBamParamBuff Buff )
	{
		final String S_ProcName = "createParam";
			CFBamParamBuff tail = null;

			CFBamParamBuff[] siblings = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
				Buff.getRequiredServerMethodId() );
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
		
		CFBamParamPKey pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredId( schema.nextParamIdGen() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamParamByUNameIdxKey keyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		keyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamParamByServerMethodIdxKey keyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		keyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamParamByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamParamByServerTypeIdxKey keyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamParamByPrevIdxKey keyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByNextIdxKey keyNextIdx = schema.getFactoryParam().newNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamParamByContPrevIdxKey keyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		keyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByContNextIdxKey keyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		keyContNextIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ParamUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredServerMethodId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			if( Buff.getOptionalTypeId() != null ) {
				allNull = false;
			}
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getOptionalTypeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Type",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictServerMethodIdx;
		if( dictByServerMethodIdx.containsKey( keyServerMethodIdx ) ) {
			subdictServerMethodIdx = dictByServerMethodIdx.get( keyServerMethodIdx );
		}
		else {
			subdictServerMethodIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerMethodIdx.put( keyServerMethodIdx, subdictServerMethodIdx );
		}
		subdictServerMethodIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictServerTypeIdx;
		if( dictByServerTypeIdx.containsKey( keyServerTypeIdx ) ) {
			subdictServerTypeIdx = dictByServerTypeIdx.get( keyServerTypeIdx );
		}
		else {
			subdictServerTypeIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerTypeIdx.put( keyServerTypeIdx, subdictServerTypeIdx );
		}
		subdictServerTypeIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictContPrevIdx;
		if( dictByContPrevIdx.containsKey( keyContPrevIdx ) ) {
			subdictContPrevIdx = dictByContPrevIdx.get( keyContPrevIdx );
		}
		else {
			subdictContPrevIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContPrevIdx.put( keyContPrevIdx, subdictContPrevIdx );
		}
		subdictContPrevIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictContNextIdx;
		if( dictByContNextIdx.containsKey( keyContNextIdx ) ) {
			subdictContNextIdx = dictByContNextIdx.get( keyContNextIdx );
		}
		else {
			subdictContNextIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContNextIdx.put( keyContNextIdx, subdictContNextIdx );
		}
		subdictContNextIdx.put( pkey, Buff );

		if( tail != null ) {
			CFBamParamBuff tailEdit = schema.getFactoryParam().newBuff();
			tailEdit.set( (CFBamParamBuff)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableParam().updateParam( Authorization, tailEdit );
		}
	}

	public CFBamParamBuff readDerived( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamParamBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff lockDerived( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamParamBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamParam.readAllDerived";
		CFBamParamBuff[] retList = new CFBamParamBuff[ dictByPKey.values().size() ];
		Iterator< CFBamParamBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamParamBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByUNameIdx";
		CFBamParamByUNameIdxKey key = schema.getFactoryParam().newUNameIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );
		key.setRequiredName( Name );

		CFBamParamBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff[] readDerivedByServerMethodIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerMethodIdx";
		CFBamParamByServerMethodIdxKey key = schema.getFactoryParam().newServerMethodIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );

		CFBamParamBuff[] recArray;
		if( dictByServerMethodIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerMethodIdx
				= dictByServerMethodIdx.get( key );
			recArray = new CFBamParamBuff[ subdictServerMethodIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictServerMethodIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerMethodIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerMethodIdx.put( key, subdictServerMethodIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByDefSchemaIdx";
		CFBamParamByDefSchemaIdxKey key = schema.getFactoryParam().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamParamBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamParamBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictDefSchemaIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByServerTypeIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerTypeIdx";
		CFBamParamByServerTypeIdxKey key = schema.getFactoryParam().newServerTypeIdxKey();
		key.setOptionalTypeId( TypeId );

		CFBamParamBuff[] recArray;
		if( dictByServerTypeIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerTypeIdx
				= dictByServerTypeIdx.get( key );
			recArray = new CFBamParamBuff[ subdictServerTypeIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictServerTypeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerTypeIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerTypeIdx.put( key, subdictServerTypeIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByPrevIdx";
		CFBamParamByPrevIdxKey key = schema.getFactoryParam().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		CFBamParamBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamParamBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictPrevIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByNextIdx";
		CFBamParamByNextIdxKey key = schema.getFactoryParam().newNextIdxKey();
		key.setOptionalNextId( NextId );

		CFBamParamBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamParamBuff[ subdictNextIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictNextIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByContPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContPrevIdx";
		CFBamParamByContPrevIdxKey key = schema.getFactoryParam().newContPrevIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalPrevId( PrevId );

		CFBamParamBuff[] recArray;
		if( dictByContPrevIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContPrevIdx
				= dictByContPrevIdx.get( key );
			recArray = new CFBamParamBuff[ subdictContPrevIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictContPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContPrevIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContPrevIdx.put( key, subdictContPrevIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByContNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContNextIdx";
		CFBamParamByContNextIdxKey key = schema.getFactoryParam().newContNextIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalNextId( NextId );

		CFBamParamBuff[] recArray;
		if( dictByContNextIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContNextIdx
				= dictByContNextIdx.get( key );
			recArray = new CFBamParamBuff[ subdictContNextIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictContNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContNextIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContNextIdx.put( key, subdictContNextIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByIdIdx() ";
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredId( Id );

		CFBamParamBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff readBuff( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "CFBamRamParam.readBuff";
		CFBamParamBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a82f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff lockBuff( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamParamBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a82f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamParam.readAllBuff";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByIdIdx() ";
		CFBamParamBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
			return( (CFBamParamBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamParamBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByUNameIdx() ";
		CFBamParamBuff buff = readDerivedByUNameIdx( Authorization,
			ServerMethodId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
			return( (CFBamParamBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamParamBuff[] readBuffByServerMethodIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByServerMethodIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByServerMethodIdx( Authorization,
			ServerMethodId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByDefSchemaIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByServerTypeIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByServerTypeIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByServerTypeIdx( Authorization,
			TypeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByPrevIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByNextIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByContPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByContPrevIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByContPrevIdx( Authorization,
			ServerMethodId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByContNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByContNextIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByContNextIdx( Authorization,
			ServerMethodId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamParamBuff moveBuffUp( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamParamBuff grandprev = null;
		CFBamParamBuff prev = null;
		CFBamParamBuff cur = null;
		CFBamParamBuff next = null;

		cur = schema.getTableParam().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamParamBuff)cur );
		}

		prev = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableParam().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamParamBuff newInstance;
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editCur = newInstance;
		editCur.set( cur );

		CFBamParamBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamParamBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
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
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamParamBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamParamBuff moveBuffDown( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamParamBuff prev = null;
		CFBamParamBuff cur = null;
		CFBamParamBuff next = null;
		CFBamParamBuff grandnext = null;

		cur = schema.getTableParam().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamParamBuff)cur );
		}

		next = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableParam().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamParamBuff newInstance;
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editNext = newInstance;
		editNext.set( next );

		CFBamParamBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamParamBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
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
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamParamBuff)editCur );
	}

	public void updateParam( CFSecAuthorization Authorization,
		CFBamParamBuff Buff )
	{
		CFBamParamPKey pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamParamBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateParam",
				"Existing record not found",
				"Param",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateParam",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamParamByUNameIdxKey existingKeyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamParamByUNameIdxKey newKeyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		newKeyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamParamByServerMethodIdxKey existingKeyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		existingKeyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamParamByServerMethodIdxKey newKeyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		newKeyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamParamByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamParamByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamParamByServerTypeIdxKey existingKeyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		existingKeyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamParamByServerTypeIdxKey newKeyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		newKeyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamParamByPrevIdxKey existingKeyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByPrevIdxKey newKeyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByNextIdxKey existingKeyNextIdx = schema.getFactoryParam().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamParamByNextIdxKey newKeyNextIdx = schema.getFactoryParam().newNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamParamByContPrevIdxKey existingKeyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		existingKeyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByContPrevIdxKey newKeyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		newKeyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByContNextIdxKey existingKeyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		existingKeyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamParamByContNextIdxKey newKeyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		newKeyContNextIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateParam",
					"ParamUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredServerMethodId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Container",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			if( Buff.getOptionalTypeId() != null ) {
				allNull = false;
			}
			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getOptionalTypeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Lookup",
						"Type",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamParamPKey, CFBamParamBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByServerMethodIdx.get( existingKeyServerMethodIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByServerMethodIdx.containsKey( newKeyServerMethodIdx ) ) {
			subdict = dictByServerMethodIdx.get( newKeyServerMethodIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerMethodIdx.put( newKeyServerMethodIdx, subdict );
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
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByServerTypeIdx.get( existingKeyServerTypeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByServerTypeIdx.containsKey( newKeyServerTypeIdx ) ) {
			subdict = dictByServerTypeIdx.get( newKeyServerTypeIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerTypeIdx.put( newKeyServerTypeIdx, subdict );
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
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
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
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByContPrevIdx.get( existingKeyContPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContPrevIdx.containsKey( newKeyContPrevIdx ) ) {
			subdict = dictByContPrevIdx.get( newKeyContPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContPrevIdx.put( newKeyContPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByContNextIdx.get( existingKeyContNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContNextIdx.containsKey( newKeyContNextIdx ) ) {
			subdict = dictByContNextIdx.get( newKeyContNextIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContNextIdx.put( newKeyContNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteParam( CFSecAuthorization Authorization,
		CFBamParamBuff Buff )
	{
		final String S_ProcName = "CFBamRamParamTable.deleteParam() ";
		String classCode;
		CFBamParamPKey pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamParamBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteParam",
				pkey );
		}
		CFLibDbKeyHash256 varServerMethodId = existing.getRequiredServerMethodId();
		CFBamServerMethodBuff container = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
			varServerMethodId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamParamBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableParam().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamParamBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				editPrev = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamParamBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableParam().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamParamBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				editNext = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamParamByUNameIdxKey keyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		keyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamParamByServerMethodIdxKey keyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		keyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamParamByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamParamByServerTypeIdxKey keyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamParamByPrevIdxKey keyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByNextIdxKey keyNextIdx = schema.getFactoryParam().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamParamByContPrevIdxKey keyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		keyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByContNextIdxKey keyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		keyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamParamPKey, CFBamParamBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByServerMethodIdx.get( keyServerMethodIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByServerTypeIdx.get( keyServerTypeIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByContPrevIdx.get( keyContPrevIdx );
		subdict.remove( pkey );

		subdict = dictByContNextIdx.get( keyContNextIdx );
		subdict.remove( pkey );

	}
	public void deleteParamByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredId( argId );
		deleteParamByIdIdx( Authorization, key );
	}

	public void deleteParamByIdIdx( CFSecAuthorization Authorization,
		CFBamParamPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamParamBuff cur;
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		String argName )
	{
		CFBamParamByUNameIdxKey key = schema.getFactoryParam().newUNameIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setRequiredName( argName );
		deleteParamByUNameIdx( Authorization, key );
	}

	public void deleteParamByUNameIdx( CFSecAuthorization Authorization,
		CFBamParamByUNameIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByServerMethodIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId )
	{
		CFBamParamByServerMethodIdxKey key = schema.getFactoryParam().newServerMethodIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		deleteParamByServerMethodIdx( Authorization, key );
	}

	public void deleteParamByServerMethodIdx( CFSecAuthorization Authorization,
		CFBamParamByServerMethodIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamParamByDefSchemaIdxKey key = schema.getFactoryParam().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteParamByDefSchemaIdx( Authorization, key );
	}

	public void deleteParamByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamParamByDefSchemaIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByServerTypeIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTypeId )
	{
		CFBamParamByServerTypeIdxKey key = schema.getFactoryParam().newServerTypeIdxKey();
		key.setOptionalTypeId( argTypeId );
		deleteParamByServerTypeIdx( Authorization, key );
	}

	public void deleteParamByServerTypeIdx( CFSecAuthorization Authorization,
		CFBamParamByServerTypeIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalTypeId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamParamByPrevIdxKey key = schema.getFactoryParam().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteParamByPrevIdx( Authorization, key );
	}

	public void deleteParamByPrevIdx( CFSecAuthorization Authorization,
		CFBamParamByPrevIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamParamByNextIdxKey key = schema.getFactoryParam().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteParamByNextIdx( Authorization, key );
	}

	public void deleteParamByNextIdx( CFSecAuthorization Authorization,
		CFBamParamByNextIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByContPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamParamByContPrevIdxKey key = schema.getFactoryParam().newContPrevIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalPrevId( argPrevId );
		deleteParamByContPrevIdx( Authorization, key );
	}

	public void deleteParamByContPrevIdx( CFSecAuthorization Authorization,
		CFBamParamByContPrevIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByContNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamParamByContNextIdxKey key = schema.getFactoryParam().newContNextIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalNextId( argNextId );
		deleteParamByContNextIdx( Authorization, key );
	}

	public void deleteParamByContNextIdx( CFSecAuthorization Authorization,
		CFBamParamByContNextIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}
}
