
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
 *	CFBamRamClearTopDepTable in-memory RAM DbIO implementation
 *	for ClearTopDep.
 */
public class CFBamRamClearTopDepTable
	implements ICFBamClearTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffClearTopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffClearTopDep >();
	private Map< CFBamBuffClearTopDepByClrTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >> dictByClrTopDepTblIdx
		= new HashMap< CFBamBuffClearTopDepByClrTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >>();
	private Map< CFBamBuffClearTopDepByUNameIdxKey,
			CFBamBuffClearTopDep > dictByUNameIdx
		= new HashMap< CFBamBuffClearTopDepByUNameIdxKey,
			CFBamBuffClearTopDep >();
	private Map< CFBamBuffClearTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >> dictByPrevIdx
		= new HashMap< CFBamBuffClearTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >>();
	private Map< CFBamBuffClearTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >> dictByNextIdx
		= new HashMap< CFBamBuffClearTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffClearTopDep >>();

	public CFBamRamClearTopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearTopDep( ICFSecAuthorization Authorization,
		ICFBamClearTopDep Buff )
	{
		final String S_ProcName = "createClearTopDep";
		ICFBamClearTopDep tail = null;
		if( Buff.getClassCode().equals( "a814" ) ) {
			ICFBamClearTopDep[] siblings = schema.getTableClearTopDep().readDerivedByClrTopDepTblIdx( Authorization,
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
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamBuffClearTopDepByClrTopDepTblIdxKey keyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		keyClrTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffClearTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffClearTopDepByPrevIdxKey keyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffClearTopDepByNextIdxKey keyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictClrTopDepTblIdx;
		if( dictByClrTopDepTblIdx.containsKey( keyClrTopDepTblIdx ) ) {
			subdictClrTopDepTblIdx = dictByClrTopDepTblIdx.get( keyClrTopDepTblIdx );
		}
		else {
			subdictClrTopDepTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByClrTopDepTblIdx.put( keyClrTopDepTblIdx, subdictClrTopDepTblIdx );
		}
		subdictClrTopDepTblIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			String tailClassCode = tail.getClassCode();
			if( tailClassCode.equals( "a814" ) ) {
				ICFBamClearTopDep tailEdit = schema.getFactoryClearTopDep().newBuff();
				tailEdit.set( (ICFBamClearTopDep)tail );
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

	public ICFBamClearTopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerived";
		ICFBamClearTopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearTopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerived";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		ICFBamClearTopDep buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearTopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearTopDep.readAllDerived";
		ICFBamClearTopDep[] retList = new ICFBamClearTopDep[ dictByPKey.values().size() ];
		Iterator< ICFBamClearTopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamClearTopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		ICFBamScope buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamScope buff;
			ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearTopDep ) ) {
					filteredList.add( (ICFBamClearTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
		}
	}

	public ICFBamClearTopDep[] readDerivedByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearTopDep ) ) {
					filteredList.add( (ICFBamClearTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
		}
	}

	public ICFBamClearTopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		ICFBamClearDep buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamClearDep buff;
			ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamClearTopDep ) ) {
					filteredList.add( (ICFBamClearTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
		}
	}

	public ICFBamClearTopDep[] readDerivedByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByClrTopDepTblIdx";
		CFBamBuffClearTopDepByClrTopDepTblIdxKey key = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		key.setRequiredTableId( TableId );

		ICFBamClearTopDep[] recArray;
		if( dictByClrTopDepTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictClrTopDepTblIdx
				= dictByClrTopDepTblIdx.get( key );
			recArray = new ICFBamClearTopDep[ subdictClrTopDepTblIdx.size() ];
			Iterator< ICFBamClearTopDep > iter = subdictClrTopDepTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictClrTopDepTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByClrTopDepTblIdx.put( key, subdictClrTopDepTblIdx );
			recArray = new ICFBamClearTopDep[0];
		}
		return( recArray );
	}

	public ICFBamClearTopDep readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByUNameIdx";
		CFBamBuffClearTopDepByUNameIdxKey key = schema.getFactoryClearTopDep().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		ICFBamClearTopDep buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearTopDep[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByPrevIdx";
		CFBamBuffClearTopDepByPrevIdxKey key = schema.getFactoryClearTopDep().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		ICFBamClearTopDep[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamClearTopDep[ subdictPrevIdx.size() ];
			Iterator< ICFBamClearTopDep > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamClearTopDep[0];
		}
		return( recArray );
	}

	public ICFBamClearTopDep[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readDerivedByNextIdx";
		CFBamBuffClearTopDepByNextIdxKey key = schema.getFactoryClearTopDep().newNextIdxKey();
		key.setOptionalNextId( NextId );

		ICFBamClearTopDep[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamClearTopDep[ subdictNextIdx.size() ];
			Iterator< ICFBamClearTopDep > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamClearTopDep[0];
		}
		return( recArray );
	}

	public ICFBamClearTopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		ICFBamClearTopDep buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearTopDep readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuff";
		ICFBamClearTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a814" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearTopDep lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamClearTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a814" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamClearTopDep[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readAllBuff";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	public ICFBamClearTopDep readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamClearTopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (ICFBamClearTopDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearTopDep[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	public ICFBamClearTopDep[] readBuffByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByClearDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	public ICFBamClearTopDep[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	public ICFBamClearTopDep[] readBuffByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByClrTopDepTblIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByClrTopDepTblIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	public ICFBamClearTopDep readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByUNameIdx() ";
		ICFBamClearTopDep buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
			return( (ICFBamClearTopDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamClearTopDep[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByPrevIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
	}

	public ICFBamClearTopDep[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamClearTopDep.readBuffByNextIdx() ";
		ICFBamClearTopDep buff;
		ArrayList<ICFBamClearTopDep> filteredList = new ArrayList<ICFBamClearTopDep>();
		ICFBamClearTopDep[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a814" ) ) {
				filteredList.add( (ICFBamClearTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamClearTopDep[0] ) );
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
	public ICFBamClearTopDep[] pageBuffByClearDepIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearTopDep[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearTopDep[] pageBuffByClrTopDepTblIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearTopDep[] pageBuffByPrevIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearTopDep[] pageBuffByNextIdx( ICFSecAuthorization Authorization,
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
	public ICFBamClearTopDep moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamClearTopDep grandprev = null;
		ICFBamClearTopDep prev = null;
		ICFBamClearTopDep cur = null;
		ICFBamClearTopDep next = null;

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
		ICFBamClearTopDep newInstance;
			if( classCode.equals( "a814" ) ) {
				newInstance = schema.getFactoryClearTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		ICFBamClearTopDep editPrev = newInstance;
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

		ICFBamClearTopDep editGrandprev = null;
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

		ICFBamClearTopDep editNext = null;
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
	public ICFBamClearTopDep moveBuffDown( ICFSecAuthorization Authorization,
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

	public void updateClearTopDep( ICFSecAuthorization Authorization,
		ICFBamClearTopDep Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamClearTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearTopDep",
				"Existing record not found",
				"ClearTopDep",
				pkey );
		}
		CFBamBuffClearTopDepByClrTopDepTblIdxKey existingKeyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		existingKeyClrTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffClearTopDepByClrTopDepTblIdxKey newKeyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		newKeyClrTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffClearTopDepByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearTopDepByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffClearTopDepByPrevIdxKey existingKeyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffClearTopDepByPrevIdxKey newKeyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffClearTopDepByNextIdxKey existingKeyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffClearTopDepByNextIdxKey newKeyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
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

		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffClearTopDep >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteClearTopDep( ICFSecAuthorization Authorization,
		ICFBamClearTopDep Buff )
	{
		final String S_ProcName = "CFBamRamClearTopDepTable.deleteClearTopDep() ";
		String classCode;
		CFLibDbKeyHash256 pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		ICFBamClearTopDep existing = dictByPKey.get( pkey );
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
		CFBamBuffClearTopDepByClrTopDepTblIdxKey keyClrTopDepTblIdx = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		keyClrTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffClearTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryClearTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffClearTopDepByPrevIdxKey keyPrevIdx = schema.getFactoryClearTopDep().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffClearTopDepByNextIdxKey keyNextIdx = schema.getFactoryClearTopDep().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffClearTopDep > subdict;

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
	public void deleteClearTopDepByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffClearTopDepByClrTopDepTblIdxKey key = schema.getFactoryClearTopDep().newClrTopDepTblIdxKey();
		key.setRequiredTableId( argTableId );
		deleteClearTopDepByClrTopDepTblIdx( Authorization, key );
	}

	public void deleteClearTopDepByClrTopDepTblIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByClrTopDepTblIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffClearTopDepByUNameIdxKey key = schema.getFactoryClearTopDep().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteClearTopDepByUNameIdx( Authorization, key );
	}

	public void deleteClearTopDepByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByUNameIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffClearTopDepByPrevIdxKey key = schema.getFactoryClearTopDep().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteClearTopDepByPrevIdx( Authorization, key );
	}

	public void deleteClearTopDepByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByPrevIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffClearTopDepByNextIdxKey key = schema.getFactoryClearTopDep().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteClearTopDepByNextIdx( Authorization, key );
	}

	public void deleteClearTopDepByNextIdx( ICFSecAuthorization Authorization,
		ICFBamClearTopDepByNextIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByClearDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteClearTopDepByClearDepIdx( Authorization, key );
	}

	public void deleteClearTopDepByClearDepIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByClearDepIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearTopDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamClearDepByDefSchemaIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFLibDbKeyHash256 key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteClearTopDepByIdIdx( Authorization, key );
	}

	public void deleteClearTopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		ICFBamClearTopDep cur;
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}

	public void deleteClearTopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearTopDepByTenantIdx( Authorization, key );
	}

	public void deleteClearTopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		ICFBamClearTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<ICFBamClearTopDep> matchSet = new LinkedList<ICFBamClearTopDep>();
		Iterator<ICFBamClearTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<ICFBamClearTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteClearTopDep( Authorization, cur );
		}
	}
}
