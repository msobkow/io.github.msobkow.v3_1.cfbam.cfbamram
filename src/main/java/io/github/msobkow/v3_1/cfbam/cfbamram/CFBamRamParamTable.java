
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
 *	CFBamRamParamTable in-memory RAM DbIO implementation
 *	for Param.
 */
public class CFBamRamParamTable
	implements ICFBamParamTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffParam > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffParam >();
	private Map< CFBamBuffParamByUNameIdxKey,
			CFBamBuffParam > dictByUNameIdx
		= new HashMap< CFBamBuffParamByUNameIdxKey,
			CFBamBuffParam >();
	private Map< CFBamBuffParamByServerMethodIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByServerMethodIdx
		= new HashMap< CFBamBuffParamByServerMethodIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByDefSchemaIdx
		= new HashMap< CFBamBuffParamByDefSchemaIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByServerTypeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByServerTypeIdx
		= new HashMap< CFBamBuffParamByServerTypeIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByPrevIdx
		= new HashMap< CFBamBuffParamByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByNextIdx
		= new HashMap< CFBamBuffParamByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByContPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByContPrevIdx
		= new HashMap< CFBamBuffParamByContPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();
	private Map< CFBamBuffParamByContNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >> dictByContNextIdx
		= new HashMap< CFBamBuffParamByContNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffParam >>();

	public CFBamRamParamTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createParam( ICFSecAuthorization Authorization,
		ICFBamParam Buff )
	{
		final String S_ProcName = "createParam";
			ICFBamParam tail = null;

			ICFBamParam[] siblings = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
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
		
		CFLibDbKeyHash256 pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredId( schema.nextParamIdGen() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamBuffParamByUNameIdxKey keyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		keyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffParamByServerMethodIdxKey keyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		keyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamBuffParamByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffParamByServerTypeIdxKey keyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamBuffParamByPrevIdxKey keyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByNextIdxKey keyNextIdx = schema.getFactoryParam().newNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffParamByContPrevIdxKey keyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		keyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByContNextIdxKey keyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerMethodIdx;
		if( dictByServerMethodIdx.containsKey( keyServerMethodIdx ) ) {
			subdictServerMethodIdx = dictByServerMethodIdx.get( keyServerMethodIdx );
		}
		else {
			subdictServerMethodIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerMethodIdx.put( keyServerMethodIdx, subdictServerMethodIdx );
		}
		subdictServerMethodIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerTypeIdx;
		if( dictByServerTypeIdx.containsKey( keyServerTypeIdx ) ) {
			subdictServerTypeIdx = dictByServerTypeIdx.get( keyServerTypeIdx );
		}
		else {
			subdictServerTypeIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerTypeIdx.put( keyServerTypeIdx, subdictServerTypeIdx );
		}
		subdictServerTypeIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContPrevIdx;
		if( dictByContPrevIdx.containsKey( keyContPrevIdx ) ) {
			subdictContPrevIdx = dictByContPrevIdx.get( keyContPrevIdx );
		}
		else {
			subdictContPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContPrevIdx.put( keyContPrevIdx, subdictContPrevIdx );
		}
		subdictContPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContNextIdx;
		if( dictByContNextIdx.containsKey( keyContNextIdx ) ) {
			subdictContNextIdx = dictByContNextIdx.get( keyContNextIdx );
		}
		else {
			subdictContNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContNextIdx.put( keyContNextIdx, subdictContNextIdx );
		}
		subdictContNextIdx.put( pkey, Buff );

		if( tail != null ) {
			ICFBamParam tailEdit = schema.getFactoryParam().newBuff();
			tailEdit.set( (ICFBamParam)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableParam().updateParam( Authorization, tailEdit );
		}
	}

	public ICFBamParam readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		ICFBamParam buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamParam lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryParam().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamParam buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamParam[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamParam.readAllDerived";
		ICFBamParam[] retList = new ICFBamParam[ dictByPKey.values().size() ];
		Iterator< ICFBamParam > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamParam readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByUNameIdx";
		CFBamBuffParamByUNameIdxKey key = schema.getFactoryParam().newUNameIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );
		key.setRequiredName( Name );

		ICFBamParam buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamParam[] readDerivedByServerMethodIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerMethodIdx";
		CFBamBuffParamByServerMethodIdxKey key = schema.getFactoryParam().newServerMethodIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );

		ICFBamParam[] recArray;
		if( dictByServerMethodIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerMethodIdx
				= dictByServerMethodIdx.get( key );
			recArray = new ICFBamParam[ subdictServerMethodIdx.size() ];
			Iterator< ICFBamParam > iter = subdictServerMethodIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerMethodIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerMethodIdx.put( key, subdictServerMethodIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByDefSchemaIdx";
		CFBamBuffParamByDefSchemaIdxKey key = schema.getFactoryParam().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( DefSchemaId );

		ICFBamParam[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new ICFBamParam[ subdictDefSchemaIdx.size() ];
			Iterator< ICFBamParam > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictDefSchemaIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam[] readDerivedByServerTypeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerTypeIdx";
		CFBamBuffParamByServerTypeIdxKey key = schema.getFactoryParam().newServerTypeIdxKey();
		key.setOptionalTypeId( TypeId );

		ICFBamParam[] recArray;
		if( dictByServerTypeIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerTypeIdx
				= dictByServerTypeIdx.get( key );
			recArray = new ICFBamParam[ subdictServerTypeIdx.size() ];
			Iterator< ICFBamParam > iter = subdictServerTypeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictServerTypeIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByServerTypeIdx.put( key, subdictServerTypeIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByPrevIdx";
		CFBamBuffParamByPrevIdxKey key = schema.getFactoryParam().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		ICFBamParam[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamParam[ subdictPrevIdx.size() ];
			Iterator< ICFBamParam > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByNextIdx";
		CFBamBuffParamByNextIdxKey key = schema.getFactoryParam().newNextIdxKey();
		key.setOptionalNextId( NextId );

		ICFBamParam[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamParam[ subdictNextIdx.size() ];
			Iterator< ICFBamParam > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam[] readDerivedByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContPrevIdx";
		CFBamBuffParamByContPrevIdxKey key = schema.getFactoryParam().newContPrevIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalPrevId( PrevId );

		ICFBamParam[] recArray;
		if( dictByContPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContPrevIdx
				= dictByContPrevIdx.get( key );
			recArray = new ICFBamParam[ subdictContPrevIdx.size() ];
			Iterator< ICFBamParam > iter = subdictContPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContPrevIdx.put( key, subdictContPrevIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam[] readDerivedByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContNextIdx";
		CFBamBuffParamByContNextIdxKey key = schema.getFactoryParam().newContNextIdxKey();
		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalNextId( NextId );

		ICFBamParam[] recArray;
		if( dictByContNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContNextIdx
				= dictByContNextIdx.get( key );
			recArray = new ICFBamParam[ subdictContNextIdx.size() ];
			Iterator< ICFBamParam > iter = subdictContNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffParam > subdictContNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContNextIdx.put( key, subdictContNextIdx );
			recArray = new ICFBamParam[0];
		}
		return( recArray );
	}

	public ICFBamParam readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryParam().newPKey();
		key.setRequiredId( Id );

		ICFBamParam buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamParam readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamParam.readBuff";
		ICFBamParam buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a82f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamParam lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamParam buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a82f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamParam[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamParam.readAllBuff";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByIdIdx() ";
		ICFBamParam buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
			return( (ICFBamParam)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamParam readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByUNameIdx() ";
		ICFBamParam buff = readDerivedByUNameIdx( Authorization,
			ServerMethodId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
			return( (ICFBamParam)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamParam[] readBuffByServerMethodIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByServerMethodIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByServerMethodIdx( Authorization,
			ServerMethodId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByDefSchemaIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam[] readBuffByServerTypeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByServerTypeIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByServerTypeIdx( Authorization,
			TypeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByPrevIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByNextIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam[] readBuffByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByContPrevIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByContPrevIdx( Authorization,
			ServerMethodId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	public ICFBamParam[] readBuffByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 ServerMethodId,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByContNextIdx() ";
		ICFBamParam buff;
		ArrayList<ICFBamParam> filteredList = new ArrayList<ICFBamParam>();
		ICFBamParam[] buffList = readDerivedByContNextIdx( Authorization,
			ServerMethodId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (ICFBamParam)buff );
			}
		}
		return( filteredList.toArray( new ICFBamParam[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamParam moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamParam grandprev = null;
		ICFBamParam prev = null;
		ICFBamParam cur = null;
		ICFBamParam next = null;

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
		ICFBamParam newInstance;
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		ICFBamParam editPrev = newInstance;
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

		ICFBamParam editGrandprev = null;
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

		ICFBamParam editNext = null;
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
	public ICFBamParam moveBuffDown( ICFSecAuthorization Authorization,
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

	public void updateParam( ICFSecAuthorization Authorization,
		ICFBamParam Buff )
	{
		CFLibDbKeyHash256 pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamParam existing = dictByPKey.get( pkey );
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
		CFBamBuffParamByUNameIdxKey existingKeyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffParamByUNameIdxKey newKeyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		newKeyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffParamByServerMethodIdxKey existingKeyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		existingKeyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamBuffParamByServerMethodIdxKey newKeyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		newKeyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamBuffParamByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffParamByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamBuffParamByServerTypeIdxKey existingKeyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		existingKeyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamBuffParamByServerTypeIdxKey newKeyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		newKeyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamBuffParamByPrevIdxKey existingKeyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByPrevIdxKey newKeyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByNextIdxKey existingKeyNextIdx = schema.getFactoryParam().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffParamByNextIdxKey newKeyNextIdx = schema.getFactoryParam().newNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamBuffParamByContPrevIdxKey existingKeyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		existingKeyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByContPrevIdxKey newKeyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		newKeyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffParamByContNextIdxKey existingKeyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		existingKeyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffParamByContNextIdxKey newKeyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffParam > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffParam >();
			dictByContNextIdx.put( newKeyContNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteParam( ICFSecAuthorization Authorization,
		ICFBamParam Buff )
	{
		final String S_ProcName = "CFBamRamParamTable.deleteParam() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamParam existing = dictByPKey.get( pkey );
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

		CFBamBuffParamByUNameIdxKey keyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		keyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffParamByServerMethodIdxKey keyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		keyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamBuffParamByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamBuffParamByServerTypeIdxKey keyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamBuffParamByPrevIdxKey keyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByNextIdxKey keyNextIdx = schema.getFactoryParam().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffParamByContPrevIdxKey keyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		keyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffParamByContNextIdxKey keyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		keyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffParam > subdict;

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
	public void deleteParamByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryParam().newPKey();
		key.setRequiredId( argId );
		deleteParamByIdIdx( Authorization, key );
	}

	public void deleteParamByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamParam cur;
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		String argName )
	{
		CFBamBuffParamByUNameIdxKey key = schema.getFactoryParam().newUNameIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setRequiredName( argName );
		deleteParamByUNameIdx( Authorization, key );
	}

	public void deleteParamByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamParamByUNameIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByServerMethodIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId )
	{
		CFBamBuffParamByServerMethodIdxKey key = schema.getFactoryParam().newServerMethodIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		deleteParamByServerMethodIdx( Authorization, key );
	}

	public void deleteParamByServerMethodIdx( ICFSecAuthorization Authorization,
		ICFBamParamByServerMethodIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffParamByDefSchemaIdxKey key = schema.getFactoryParam().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteParamByDefSchemaIdx( Authorization, key );
	}

	public void deleteParamByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamParamByDefSchemaIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByServerTypeIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTypeId )
	{
		CFBamBuffParamByServerTypeIdxKey key = schema.getFactoryParam().newServerTypeIdxKey();
		key.setOptionalTypeId( argTypeId );
		deleteParamByServerTypeIdx( Authorization, key );
	}

	public void deleteParamByServerTypeIdx( ICFSecAuthorization Authorization,
		ICFBamParamByServerTypeIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalTypeId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffParamByPrevIdxKey key = schema.getFactoryParam().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteParamByPrevIdx( Authorization, key );
	}

	public void deleteParamByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamParamByPrevIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffParamByNextIdxKey key = schema.getFactoryParam().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteParamByNextIdx( Authorization, key );
	}

	public void deleteParamByNextIdx( ICFSecAuthorization Authorization,
		ICFBamParamByNextIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByContPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffParamByContPrevIdxKey key = schema.getFactoryParam().newContPrevIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalPrevId( argPrevId );
		deleteParamByContPrevIdx( Authorization, key );
	}

	public void deleteParamByContPrevIdx( ICFSecAuthorization Authorization,
		ICFBamParamByContPrevIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByContNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argServerMethodId,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffParamByContNextIdxKey key = schema.getFactoryParam().newContNextIdxKey();
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalNextId( argNextId );
		deleteParamByContNextIdx( Authorization, key );
	}

	public void deleteParamByContNextIdx( ICFSecAuthorization Authorization,
		ICFBamParamByContNextIdxKey argKey )
	{
		ICFBamParam cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamParam> matchSet = new LinkedList<ICFBamParam>();
		Iterator<ICFBamParam> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamParam> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}
}
