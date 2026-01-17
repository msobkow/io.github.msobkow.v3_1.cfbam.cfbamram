
// Description: Java 25 in-memory RAM DbIO implementation for ClearTopDep.

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
 *	CFBamRamClearTopDepTable in-memory RAM DbIO implementation
 *	for ClearTopDep.
 */
public class CFBamRamClearTopDepTable
	implements ICFBamClearTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamClearTopDepBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamClearTopDepBuff >();
	private Map< CFBamClearTopDepByClrTopDepTblIdxKey,
				Map< CFBamScopePKey,
					CFBamClearTopDepBuff >> dictByClrTopDepTblIdx
		= new HashMap< CFBamClearTopDepByClrTopDepTblIdxKey,
				Map< CFBamScopePKey,
					CFBamClearTopDepBuff >>();
	private Map< CFBamClearTopDepByUNameIdxKey,
			CFBamClearTopDepBuff > dictByUNameIdx
		= new HashMap< CFBamClearTopDepByUNameIdxKey,
			CFBamClearTopDepBuff >();
	private Map< CFBamClearTopDepByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamClearTopDepBuff >> dictByPrevIdx
		= new HashMap< CFBamClearTopDepByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamClearTopDepBuff >>();
	private Map< CFBamClearTopDepByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamClearTopDepBuff >> dictByNextIdx
		= new HashMap< CFBamClearTopDepByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamClearTopDepBuff >>();

	public CFBamRamClearTopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearTopDep( CFSecAuthorization Authorization,
		CFBamClearTopDepBuff Buff )
	{
		final String S_ProcName = "createClearTopDep";
		CFBamClearTopDepBuff tail = null;
		if( Buff.getClassCode().equals( "a814" ) ) {
			CFBamClearTopDepBuff[] siblings = schema.getTableClearTopDep().readDerivedByClrTopDepTblIdx( Authorization,
				Buff.getRequiredTableId() );
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
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearTopDepByClrTopDepTblIdxKey keyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		keyClrTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamClearTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamClearTopDepByPrevIdxKey keyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamClearTopDepByNextIdxKey keyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearTopDepUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"ClearDep",
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
						"Table",
						"Table",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictClrTopDepTblIdx;
		if( dictByClrTopDepTblIdx.containsKey( keyClrTopDepTblIdx ) ) {
			subdictClrTopDepTblIdx = dictByClrTopDepTblIdx.get( keyClrTopDepTblIdx );
		}
		else {
			subdictClrTopDepTblIdx = new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByClrTopDepTblIdx.put( keyClrTopDepTblIdx, subdictClrTopDepTblIdx );
		}
		subdictClrTopDepTblIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			String tailClassCode = tail.getClassCode();
			if( tailClassCode.equals( "a814" ) ) {
				CFBamClearTopDepBuff tailEdit = schema.getFactoryClearTopDep().newBuff();
				tailEdit.set( (CFBamClearTopDepBuff)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableClearTopDep().updateClearTopDep( Authorization, tailEdit );
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode " + tailClassCode );
			}
		}
	}

	public CFBamClearTopDepBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearTopDepBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearTopDepBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearTopDep.readAllDerived";
		CFBamClearTopDepBuff[] retList = new CFBamClearTopDepBuff[ dictByPKey.values().size() ];
		Iterator< CFBamClearTopDepBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamClearTopDepBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearTopDepBuff ) ) {
					filteredList.add( (CFBamClearTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
		}
	}

	public CFBamClearTopDepBuff[] readDerivedByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		CFBamClearDepBuff buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamClearDepBuff buff;
			ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearTopDepBuff ) ) {
					filteredList.add( (CFBamClearTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
		}
	}

	public CFBamClearTopDepBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		CFBamClearDepBuff buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamClearDepBuff buff;
			ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearTopDepBuff ) ) {
					filteredList.add( (CFBamClearTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
		}
	}

	public CFBamClearTopDepBuff[] readDerivedByClrTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByClrTopDepTblIdx";
		CFBamClearTopDepByClrTopDepTblIdxKey key = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		key.setRequiredTableId( TableId );

		CFBamClearTopDepBuff[] recArray;
		if( dictByClrTopDepTblIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictClrTopDepTblIdx
				= dictByClrTopDepTblIdx.get( key );
			recArray = new CFBamClearTopDepBuff[ subdictClrTopDepTblIdx.size() ];
			Iterator< CFBamClearTopDepBuff > iter = subdictClrTopDepTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictClrTopDepTblIdx
				= new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByClrTopDepTblIdx.put( key, subdictClrTopDepTblIdx );
			recArray = new CFBamClearTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamClearTopDepBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByUNameIdx";
		CFBamClearTopDepByUNameIdxKey key = schema.getFactoryClearTopDep().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamClearTopDepBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearTopDepBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByPrevIdx";
		CFBamClearTopDepByPrevIdxKey key = schema.getFactoryClearTopDep().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		CFBamClearTopDepBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamClearTopDepBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamClearTopDepBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictPrevIdx
				= new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamClearTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamClearTopDepBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByNextIdx";
		CFBamClearTopDepByNextIdxKey key = schema.getFactoryClearTopDep().newNextIdxKey();
		key.setOptionalNextId( NextId );

		CFBamClearTopDepBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamClearTopDepBuff[ subdictNextIdx.size() ];
			Iterator< CFBamClearTopDepBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearTopDepBuff > subdictNextIdx
				= new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamClearTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamClearTopDepBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamClearTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearTopDepBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuff";
		CFBamClearTopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a814" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearTopDepBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamClearTopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a814" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearTopDepBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readAllBuff";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	public CFBamClearTopDepBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamClearTopDepBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamClearTopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearTopDepBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamClearTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	public CFBamClearTopDepBuff[] readBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	public CFBamClearTopDepBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	public CFBamClearTopDepBuff[] readBuffByClrTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByClrTopDepTblIdx() ";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readDerivedByClrTopDepTblIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( (CFBamClearTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	public CFBamClearTopDepBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByUNameIdx() ";
		CFBamClearTopDepBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
			return( (CFBamClearTopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearTopDepBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByPrevIdx() ";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( (CFBamClearTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	public CFBamClearTopDepBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByNextIdx() ";
		CFBamClearTopDepBuff buff;
		ArrayList<CFBamClearTopDepBuff> filteredList = new ArrayList<CFBamClearTopDepBuff>();
		CFBamClearTopDepBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( (CFBamClearTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearTopDepBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific ClearTopDep buffer instances identified by the duplicate key ClearDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The ClearTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearTopDepBuff[] pageBuffByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClearDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearTopDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ClearTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearTopDepBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearTopDep buffer instances identified by the duplicate key ClrTopDepTblIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The ClearTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearTopDepBuff[] pageBuffByClrTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByClrTopDepTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearTopDep buffer instances identified by the duplicate key PrevIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PrevId	The ClearTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearTopDepBuff[] pageBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPrevIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearTopDep buffer instances identified by the duplicate key NextIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	NextId	The ClearTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearTopDepBuff[] pageBuffByNextIdx( CFSecAuthorization Authorization,
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
	public CFBamClearTopDepBuff moveBuffUp( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamClearTopDepBuff grandprev = null;
		CFBamClearTopDepBuff prev = null;
		CFBamClearTopDepBuff cur = null;
		CFBamClearTopDepBuff next = null;

		cur = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamClearTopDepBuff)cur );
		}

		prev = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamClearTopDepBuff newInstance;
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamClearTopDepBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamClearTopDepBuff editCur = newInstance;
		editCur.set( cur );

		CFBamClearTopDepBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamClearTopDepBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
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
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamClearTopDepBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamClearTopDepBuff moveBuffDown( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamClearTopDepBuff prev = null;
		CFBamClearTopDepBuff cur = null;
		CFBamClearTopDepBuff next = null;
		CFBamClearTopDepBuff grandnext = null;

		cur = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamClearTopDepBuff)cur );
		}

		next = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableClearTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamClearTopDepBuff newInstance;
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamClearTopDepBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamClearTopDepBuff editNext = newInstance;
		editNext.set( next );

		CFBamClearTopDepBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamClearTopDepBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
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
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamClearTopDepBuff)editCur );
	}

	public void updateClearTopDep( CFSecAuthorization Authorization,
		CFBamClearTopDepBuff Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearTopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearTopDep",
				"Existing record not found",
				"ClearTopDep",
				pkey );
		}
		CFBamClearTopDepByClrTopDepTblIdxKey existingKeyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		existingKeyClrTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamClearTopDepByClrTopDepTblIdxKey newKeyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		newKeyClrTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamClearTopDepByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamClearTopDepByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamClearTopDepByPrevIdxKey existingKeyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamClearTopDepByPrevIdxKey newKeyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamClearTopDepByNextIdxKey existingKeyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamClearTopDepByNextIdxKey newKeyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearTopDep",
					"ClearTopDepUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearTopDep",
						"Superclass",
						"SuperClass",
						"ClearDep",
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
						"updateClearTopDep",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamClearTopDepBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClrTopDepTblIdx.get( existingKeyClrTopDepTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClrTopDepTblIdx.containsKey( newKeyClrTopDepTblIdx ) ) {
			subdict = dictByClrTopDepTblIdx.get( newKeyClrTopDepTblIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByClrTopDepTblIdx.put( newKeyClrTopDepTblIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
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
			subdict = new HashMap< CFBamScopePKey, CFBamClearTopDepBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteClearTopDep( CFSecAuthorization Authorization,
		CFBamClearTopDepBuff Buff )
	{
		final String S_ProcName = "CFBamRamClearTopDepTable.deleteClearTopDep() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearTopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearTopDep",
				pkey );
		}
		CFLibDbKeyHash256 varTableId = existing.getRequiredTableId();
		CFBamTableBuff container = schema.getTableTable().readDerivedByIdIdx( Authorization,
			varTableId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamClearTopDepBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamClearTopDepBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a814" ) ) {
				editPrev = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamClearTopDepBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamClearTopDepBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a814" ) ) {
				editNext = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a814" ) ) {
				schema.getTableClearTopDep().updateClearTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep1().readDerivedByClearTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep1().deleteClearSubDep1ByClearTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamClearTopDepByClrTopDepTblIdxKey keyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		keyClrTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamClearTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamClearTopDepByPrevIdxKey keyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamClearTopDepByNextIdxKey keyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamClearTopDepBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClrTopDepTblIdx.get( keyClrTopDepTblIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearTopDepByClrTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamClearTopDepByClrTopDepTblIdxKey key = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		key.setRequiredTableId( argTableId );
		deleteClearTopDepByClrTopDepTblIdx( Authorization, key );
	}

	public void deleteClearTopDepByClrTopDepTblIdx( CFSecAuthorization Authorization,
		CFBamClearTopDepByClrTopDepTblIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamClearTopDepByUNameIdxKey key = schema.getFactoryClearTopDep().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteClearTopDepByUNameIdx( Authorization, key );
	}

	public void deleteClearTopDepByUNameIdx( CFSecAuthorization Authorization,
		CFBamClearTopDepByUNameIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamClearTopDepByPrevIdxKey key = schema.getFactoryClearTopDep().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteClearTopDepByPrevIdx( Authorization, key );
	}

	public void deleteClearTopDepByPrevIdx( CFSecAuthorization Authorization,
		CFBamClearTopDepByPrevIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamClearTopDepByNextIdxKey key = schema.getFactoryClearTopDep().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteClearTopDepByNextIdx( Authorization, key );
	}

	public void deleteClearTopDepByNextIdx( CFSecAuthorization Authorization,
		CFBamClearTopDepByNextIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByClearDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearTopDepByClearDepIdx( Authorization, key );
	}

	public void deleteClearTopDepByClearDepIdx( CFSecAuthorization Authorization,
		CFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearTopDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearTopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearTopDepByIdIdx( Authorization, key );
	}

	public void deleteClearTopDepByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamClearTopDepBuff cur;
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearTopDepByTenantIdx( Authorization, key );
	}

	public void deleteClearTopDepByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamClearTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearTopDepBuff> matchSet = new LinkedList<CFBamClearTopDepBuff>();
		Iterator<CFBamClearTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}
}
