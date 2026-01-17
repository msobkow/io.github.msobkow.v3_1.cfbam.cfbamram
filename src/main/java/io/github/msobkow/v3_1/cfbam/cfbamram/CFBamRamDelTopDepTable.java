
// Description: Java 25 in-memory RAM DbIO implementation for DelTopDep.

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
 *	CFBamRamDelTopDepTable in-memory RAM DbIO implementation
 *	for DelTopDep.
 */
public class CFBamRamDelTopDepTable
	implements ICFBamDelTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamDelTopDepBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamDelTopDepBuff >();
	private Map< CFBamDelTopDepByDelTopDepTblIdxKey,
				Map< CFBamScopePKey,
					CFBamDelTopDepBuff >> dictByDelTopDepTblIdx
		= new HashMap< CFBamDelTopDepByDelTopDepTblIdxKey,
				Map< CFBamScopePKey,
					CFBamDelTopDepBuff >>();
	private Map< CFBamDelTopDepByUNameIdxKey,
			CFBamDelTopDepBuff > dictByUNameIdx
		= new HashMap< CFBamDelTopDepByUNameIdxKey,
			CFBamDelTopDepBuff >();
	private Map< CFBamDelTopDepByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamDelTopDepBuff >> dictByPrevIdx
		= new HashMap< CFBamDelTopDepByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamDelTopDepBuff >>();
	private Map< CFBamDelTopDepByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamDelTopDepBuff >> dictByNextIdx
		= new HashMap< CFBamDelTopDepByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamDelTopDepBuff >>();

	public CFBamRamDelTopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createDelTopDep( CFSecAuthorization Authorization,
		CFBamDelTopDepBuff Buff )
	{
		final String S_ProcName = "createDelTopDep";
		CFBamDelTopDepBuff tail = null;
		if( Buff.getClassCode().equals( "a81b" ) ) {
			CFBamDelTopDepBuff[] siblings = schema.getTableDelTopDep().readDerivedByDelTopDepTblIdx( Authorization,
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
		schema.getTableDelDep().createDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelTopDepByDelTopDepTblIdxKey keyDelTopDepTblIdx = schema.getFactoryDelTopDep().newDelTopDepTblIdxKey();
		keyDelTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamDelTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryDelTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamDelTopDepByPrevIdxKey keyPrevIdx = schema.getFactoryDelTopDep().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamDelTopDepByNextIdxKey keyNextIdx = schema.getFactoryDelTopDep().newNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelTopDepUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"DelDep",
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

		Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictDelTopDepTblIdx;
		if( dictByDelTopDepTblIdx.containsKey( keyDelTopDepTblIdx ) ) {
			subdictDelTopDepTblIdx = dictByDelTopDepTblIdx.get( keyDelTopDepTblIdx );
		}
		else {
			subdictDelTopDepTblIdx = new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByDelTopDepTblIdx.put( keyDelTopDepTblIdx, subdictDelTopDepTblIdx );
		}
		subdictDelTopDepTblIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			String tailClassCode = tail.getClassCode();
			if( tailClassCode.equals( "a81b" ) ) {
				CFBamDelTopDepBuff tailEdit = schema.getFactoryDelTopDep().newBuff();
				tailEdit.set( (CFBamDelTopDepBuff)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDelTopDep().updateDelTopDep( Authorization, tailEdit );
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode " + tailClassCode );
			}
		}
	}

	public CFBamDelTopDepBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelTopDepBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelTopDepBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelTopDep.readAllDerived";
		CFBamDelTopDepBuff[] retList = new CFBamDelTopDepBuff[ dictByPKey.values().size() ];
		Iterator< CFBamDelTopDepBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamDelTopDepBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelTopDepBuff ) ) {
					filteredList.add( (CFBamDelTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
		}
	}

	public CFBamDelTopDepBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		CFBamDelDepBuff buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamDelDepBuff buff;
			ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelTopDepBuff ) ) {
					filteredList.add( (CFBamDelTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
		}
	}

	public CFBamDelTopDepBuff[] readDerivedByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		CFBamDelDepBuff buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamDelDepBuff buff;
			ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelTopDepBuff ) ) {
					filteredList.add( (CFBamDelTopDepBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
		}
	}

	public CFBamDelTopDepBuff[] readDerivedByDelTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByDelTopDepTblIdx";
		CFBamDelTopDepByDelTopDepTblIdxKey key = schema.getFactoryDelTopDep().newDelTopDepTblIdxKey();
		key.setRequiredTableId( TableId );

		CFBamDelTopDepBuff[] recArray;
		if( dictByDelTopDepTblIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictDelTopDepTblIdx
				= dictByDelTopDepTblIdx.get( key );
			recArray = new CFBamDelTopDepBuff[ subdictDelTopDepTblIdx.size() ];
			Iterator< CFBamDelTopDepBuff > iter = subdictDelTopDepTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictDelTopDepTblIdx
				= new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByDelTopDepTblIdx.put( key, subdictDelTopDepTblIdx );
			recArray = new CFBamDelTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamDelTopDepBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByUNameIdx";
		CFBamDelTopDepByUNameIdxKey key = schema.getFactoryDelTopDep().newUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamDelTopDepBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelTopDepBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByPrevIdx";
		CFBamDelTopDepByPrevIdxKey key = schema.getFactoryDelTopDep().newPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		CFBamDelTopDepBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamDelTopDepBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamDelTopDepBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictPrevIdx
				= new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamDelTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamDelTopDepBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByNextIdx";
		CFBamDelTopDepByNextIdxKey key = schema.getFactoryDelTopDep().newNextIdxKey();
		key.setOptionalNextId( NextId );

		CFBamDelTopDepBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamDelTopDepBuff[ subdictNextIdx.size() ];
			Iterator< CFBamDelTopDepBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelTopDepBuff > subdictNextIdx
				= new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamDelTopDepBuff[0];
		}
		return( recArray );
	}

	public CFBamDelTopDepBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( Id );

		CFBamDelTopDepBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelTopDepBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuff";
		CFBamDelTopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a81b" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelTopDepBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamDelTopDepBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a81b" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelTopDepBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readAllBuff";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81b" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	public CFBamDelTopDepBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamDelTopDepBuff buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamDelTopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamDelTopDepBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamDelTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	public CFBamDelTopDepBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	public CFBamDelTopDepBuff[] readBuffByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	public CFBamDelTopDepBuff[] readBuffByDelTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByDelTopDepTblIdx() ";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readDerivedByDelTopDepTblIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81b" ) ) {
				filteredList.add( (CFBamDelTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	public CFBamDelTopDepBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByUNameIdx() ";
		CFBamDelTopDepBuff buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a81b" ) ) {
			return( (CFBamDelTopDepBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamDelTopDepBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByPrevIdx() ";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81b" ) ) {
				filteredList.add( (CFBamDelTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	public CFBamDelTopDepBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByNextIdx() ";
		CFBamDelTopDepBuff buff;
		ArrayList<CFBamDelTopDepBuff> filteredList = new ArrayList<CFBamDelTopDepBuff>();
		CFBamDelTopDepBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a81b" ) ) {
				filteredList.add( (CFBamDelTopDepBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelTopDepBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific DelTopDep buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The DelTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelTopDepBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelTopDep buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	RelationId	The DelTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelTopDepBuff[] pageBuffByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelTopDep buffer instances identified by the duplicate key DelTopDepTblIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The DelTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelTopDepBuff[] pageBuffByDelTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByDelTopDepTblIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelTopDep buffer instances identified by the duplicate key PrevIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	PrevId	The DelTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelTopDepBuff[] pageBuffByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageBuffByPrevIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelTopDep buffer instances identified by the duplicate key NextIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	NextId	The DelTopDep key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelTopDepBuff[] pageBuffByNextIdx( CFSecAuthorization Authorization,
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
	public CFBamDelTopDepBuff moveBuffUp( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamDelTopDepBuff grandprev = null;
		CFBamDelTopDepBuff prev = null;
		CFBamDelTopDepBuff cur = null;
		CFBamDelTopDepBuff next = null;

		cur = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamDelTopDepBuff)cur );
		}

		prev = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamDelTopDepBuff newInstance;
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamDelTopDepBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamDelTopDepBuff editCur = newInstance;
		editCur.set( cur );

		CFBamDelTopDepBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamDelTopDepBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
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
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamDelTopDepBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamDelTopDepBuff moveBuffDown( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamDelTopDepBuff prev = null;
		CFBamDelTopDepBuff cur = null;
		CFBamDelTopDepBuff next = null;
		CFBamDelTopDepBuff grandnext = null;

		cur = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamDelTopDepBuff)cur );
		}

		next = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamDelTopDepBuff newInstance;
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamDelTopDepBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamDelTopDepBuff editNext = newInstance;
		editNext.set( next );

		CFBamDelTopDepBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamDelTopDepBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
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
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamDelTopDepBuff)editCur );
	}

	public void updateDelTopDep( CFSecAuthorization Authorization,
		CFBamDelTopDepBuff Buff )
	{
		schema.getTableDelDep().updateDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelTopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelTopDep",
				"Existing record not found",
				"DelTopDep",
				pkey );
		}
		CFBamDelTopDepByDelTopDepTblIdxKey existingKeyDelTopDepTblIdx = schema.getFactoryDelTopDep().newDelTopDepTblIdxKey();
		existingKeyDelTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamDelTopDepByDelTopDepTblIdxKey newKeyDelTopDepTblIdx = schema.getFactoryDelTopDep().newDelTopDepTblIdxKey();
		newKeyDelTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamDelTopDepByUNameIdxKey existingKeyUNameIdx = schema.getFactoryDelTopDep().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamDelTopDepByUNameIdxKey newKeyUNameIdx = schema.getFactoryDelTopDep().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamDelTopDepByPrevIdxKey existingKeyPrevIdx = schema.getFactoryDelTopDep().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamDelTopDepByPrevIdxKey newKeyPrevIdx = schema.getFactoryDelTopDep().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamDelTopDepByNextIdxKey existingKeyNextIdx = schema.getFactoryDelTopDep().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamDelTopDepByNextIdxKey newKeyNextIdx = schema.getFactoryDelTopDep().newNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelTopDep",
					"DelTopDepUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelTopDep",
						"Superclass",
						"SuperClass",
						"DelDep",
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
						"updateDelTopDep",
						"Container",
						"Table",
						"Table",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamDelTopDepBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDelTopDepTblIdx.get( existingKeyDelTopDepTblIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelTopDepTblIdx.containsKey( newKeyDelTopDepTblIdx ) ) {
			subdict = dictByDelTopDepTblIdx.get( newKeyDelTopDepTblIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByDelTopDepTblIdx.put( newKeyDelTopDepTblIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
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
			subdict = new HashMap< CFBamScopePKey, CFBamDelTopDepBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteDelTopDep( CFSecAuthorization Authorization,
		CFBamDelTopDepBuff Buff )
	{
		final String S_ProcName = "CFBamRamDelTopDepTable.deleteDelTopDep() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelTopDepBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelTopDep",
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

		CFBamDelTopDepBuff prev = null;
		if( ( prevId != null ) )
		{
			prev = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamDelTopDepBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				editPrev = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamDelTopDepBuff next = null;
		if( ( nextId != null ) )
		{
			next = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamDelTopDepBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				editNext = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep1().readDerivedByDelTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep1().deleteDelSubDep1ByDelTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamDelTopDepByDelTopDepTblIdxKey keyDelTopDepTblIdx = schema.getFactoryDelTopDep().newDelTopDepTblIdxKey();
		keyDelTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamDelTopDepByUNameIdxKey keyUNameIdx = schema.getFactoryDelTopDep().newUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamDelTopDepByPrevIdxKey keyPrevIdx = schema.getFactoryDelTopDep().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamDelTopDepByNextIdxKey keyNextIdx = schema.getFactoryDelTopDep().newNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamDelTopDepBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDelTopDepTblIdx.get( keyDelTopDepTblIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	public void deleteDelTopDepByDelTopDepTblIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamDelTopDepByDelTopDepTblIdxKey key = schema.getFactoryDelTopDep().newDelTopDepTblIdxKey();
		key.setRequiredTableId( argTableId );
		deleteDelTopDepByDelTopDepTblIdx( Authorization, key );
	}

	public void deleteDelTopDepByDelTopDepTblIdx( CFSecAuthorization Authorization,
		CFBamDelTopDepByDelTopDepTblIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByUNameIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamDelTopDepByUNameIdxKey key = schema.getFactoryDelTopDep().newUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteDelTopDepByUNameIdx( Authorization, key );
	}

	public void deleteDelTopDepByUNameIdx( CFSecAuthorization Authorization,
		CFBamDelTopDepByUNameIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByPrevIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamDelTopDepByPrevIdxKey key = schema.getFactoryDelTopDep().newPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteDelTopDepByPrevIdx( Authorization, key );
	}

	public void deleteDelTopDepByPrevIdx( CFSecAuthorization Authorization,
		CFBamDelTopDepByPrevIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByNextIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamDelTopDepByNextIdxKey key = schema.getFactoryDelTopDep().newNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteDelTopDepByNextIdx( Authorization, key );
	}

	public void deleteDelTopDepByNextIdx( CFSecAuthorization Authorization,
		CFBamDelTopDepByNextIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelTopDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelTopDepByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByDelDepIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelTopDepByDelDepIdx( Authorization, key );
	}

	public void deleteDelTopDepByDelDepIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByIdIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredId( argId );
		deleteDelTopDepByIdIdx( Authorization, key );
	}

	public void deleteDelTopDepByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamDelTopDepBuff cur;
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByTenantIdx( CFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelTopDepByTenantIdx( Authorization, key );
	}

	public void deleteDelTopDepByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamDelTopDepBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelTopDepBuff> matchSet = new LinkedList<CFBamDelTopDepBuff>();
		Iterator<CFBamDelTopDepBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelTopDepBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() );
			deleteDelTopDep( Authorization, cur );
		}
	}
}
