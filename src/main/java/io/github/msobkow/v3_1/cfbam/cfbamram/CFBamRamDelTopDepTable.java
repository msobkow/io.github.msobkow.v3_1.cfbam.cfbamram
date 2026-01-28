
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
 *	CFBamRamDelTopDepTable in-memory RAM DbIO implementation
 *	for DelTopDep.
 */
public class CFBamRamDelTopDepTable
	implements ICFBamDelTopDepTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffDelTopDep > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffDelTopDep >();
	private Map< CFBamBuffDelTopDepByDelTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >> dictByDelTopDepTblIdx
		= new HashMap< CFBamBuffDelTopDepByDelTopDepTblIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >>();
	private Map< CFBamBuffDelTopDepByUNameIdxKey,
			CFBamBuffDelTopDep > dictByUNameIdx
		= new HashMap< CFBamBuffDelTopDepByUNameIdxKey,
			CFBamBuffDelTopDep >();
	private Map< CFBamBuffDelTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >> dictByPrevIdx
		= new HashMap< CFBamBuffDelTopDepByPrevIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >>();
	private Map< CFBamBuffDelTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >> dictByNextIdx
		= new HashMap< CFBamBuffDelTopDepByNextIdxKey,
				Map< CFLibDbKeyHash256,
					CFBamBuffDelTopDep >>();

	public CFBamRamDelTopDepTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public ICFBamDelTopDep createDelTopDep( ICFSecAuthorization Authorization,
		ICFBamDelTopDep iBuff )
	{
		final String S_ProcName = "createDelTopDep";
		
		CFBamBuffDelTopDep Buff = (CFBamBuffDelTopDep)(schema.getTableDelDep().createDelDep( Authorization,
			iBuff ));
		ICFBamDelTopDep tail = null;
		if( Buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
			ICFBamDelTopDep[] siblings = schema.getTableDelTopDep().readDerivedByDelTopDepTblIdx( Authorization,
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
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		CFBamBuffDelTopDepByDelTopDepTblIdxKey keyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		keyDelTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffDelTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getFactoryDelTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffDelTopDepByPrevIdxKey keyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getFactoryDelTopDep().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffDelTopDepByNextIdxKey keyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getFactoryDelTopDep().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelTopDepUNameIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictDelTopDepTblIdx;
		if( dictByDelTopDepTblIdx.containsKey( keyDelTopDepTblIdx ) ) {
			subdictDelTopDepTblIdx = dictByDelTopDepTblIdx.get( keyDelTopDepTblIdx );
		}
		else {
			subdictDelTopDepTblIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByDelTopDepTblIdx.put( keyDelTopDepTblIdx, subdictDelTopDepTblIdx );
		}
		subdictDelTopDepTblIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			int tailClassCode = tail.getClassCode();
			if( tailClassCode == ICFBamDelTopDep.CLASS_CODE ) {
				ICFBamDelTopDep tailEdit = schema.getFactoryDelTopDep().newBuff();
				tailEdit.set( (ICFBamDelTopDep)tail );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableDelTopDep().updateDelTopDep( Authorization, tailEdit );
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode " + tailClassCode );
			}
		}
		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamDelTopDep.CLASS_CODE) {
				CFBamBuffDelTopDep retbuff = ((CFBamBuffDelTopDep)(schema.getFactoryDelTopDep().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamDelTopDep readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerived";
		ICFBamDelTopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelTopDep lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerived";
		ICFBamDelTopDep buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelTopDep[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelTopDep.readAllDerived";
		ICFBamDelTopDep[] retList = new ICFBamDelTopDep[ dictByPKey.values().size() ];
		Iterator< CFBamBuffDelTopDep > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamDelTopDep[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelTopDep ) ) {
					filteredList.add( (ICFBamDelTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
		}
	}

	public ICFBamDelTopDep[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelTopDep ) ) {
					filteredList.add( (ICFBamDelTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
		}
	}

	public ICFBamDelTopDep[] readDerivedByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		ICFBamDelDep buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamDelDep buff;
			ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamDelTopDep ) ) {
					filteredList.add( (ICFBamDelTopDep)buff );
				}
			}
			return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
		}
	}

	public ICFBamDelTopDep[] readDerivedByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByDelTopDepTblIdx";
		CFBamBuffDelTopDepByDelTopDepTblIdxKey key = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		key.setRequiredTableId( TableId );

		ICFBamDelTopDep[] recArray;
		if( dictByDelTopDepTblIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictDelTopDepTblIdx
				= dictByDelTopDepTblIdx.get( key );
			recArray = new ICFBamDelTopDep[ subdictDelTopDepTblIdx.size() ];
			Iterator< CFBamBuffDelTopDep > iter = subdictDelTopDepTblIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictDelTopDepTblIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByDelTopDepTblIdx.put( key, subdictDelTopDepTblIdx );
			recArray = new ICFBamDelTopDep[0];
		}
		return( recArray );
	}

	public ICFBamDelTopDep readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByUNameIdx";
		CFBamBuffDelTopDepByUNameIdxKey key = (CFBamBuffDelTopDepByUNameIdxKey)schema.getFactoryDelTopDep().newByUNameIdxKey();
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		ICFBamDelTopDep buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelTopDep[] readDerivedByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByPrevIdx";
		CFBamBuffDelTopDepByPrevIdxKey key = (CFBamBuffDelTopDepByPrevIdxKey)schema.getFactoryDelTopDep().newByPrevIdxKey();
		key.setOptionalPrevId( PrevId );

		ICFBamDelTopDep[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new ICFBamDelTopDep[ subdictPrevIdx.size() ];
			Iterator< CFBamBuffDelTopDep > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictPrevIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new ICFBamDelTopDep[0];
		}
		return( recArray );
	}

	public ICFBamDelTopDep[] readDerivedByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readDerivedByNextIdx";
		CFBamBuffDelTopDepByNextIdxKey key = (CFBamBuffDelTopDepByNextIdxKey)schema.getFactoryDelTopDep().newByNextIdxKey();
		key.setOptionalNextId( NextId );

		ICFBamDelTopDep[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new ICFBamDelTopDep[ subdictNextIdx.size() ];
			Iterator< CFBamBuffDelTopDep > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdictNextIdx
				= new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new ICFBamDelTopDep[0];
		}
		return( recArray );
	}

	public ICFBamDelTopDep readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamDelTopDep buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelTopDep readBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuff";
		ICFBamDelTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelTopDep lockBuff( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockBuff";
		ICFBamDelTopDep buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamDelTopDep.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamDelTopDep[] readAllBuff( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readAllBuff";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	public ICFBamDelTopDep readBuffByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		ICFBamDelTopDep buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamDelTopDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelTopDep[] readBuffByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	public ICFBamDelTopDep[] readBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	public ICFBamDelTopDep[] readBuffByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	public ICFBamDelTopDep[] readBuffByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByDelTopDepTblIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByDelTopDepTblIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	public ICFBamDelTopDep readBuffByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByUNameIdx() ";
		ICFBamDelTopDep buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
			return( (ICFBamDelTopDep)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamDelTopDep[] readBuffByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PrevId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByPrevIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByPrevIdx( Authorization,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
	}

	public ICFBamDelTopDep[] readBuffByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 NextId )
	{
		final String S_ProcName = "CFBamRamDelTopDep.readBuffByNextIdx() ";
		ICFBamDelTopDep buff;
		ArrayList<ICFBamDelTopDep> filteredList = new ArrayList<ICFBamDelTopDep>();
		ICFBamDelTopDep[] buffList = readDerivedByNextIdx( Authorization,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamDelTopDep.CLASS_CODE ) ) {
				filteredList.add( (ICFBamDelTopDep)buff );
			}
		}
		return( filteredList.toArray( new ICFBamDelTopDep[0] ) );
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
	public ICFBamDelTopDep[] pageBuffByDefSchemaIdx( ICFSecAuthorization Authorization,
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
	public ICFBamDelTopDep[] pageBuffByDelDepIdx( ICFSecAuthorization Authorization,
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
	public ICFBamDelTopDep[] pageBuffByDelTopDepTblIdx( ICFSecAuthorization Authorization,
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
	public ICFBamDelTopDep[] pageBuffByPrevIdx( ICFSecAuthorization Authorization,
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
	public ICFBamDelTopDep[] pageBuffByNextIdx( ICFSecAuthorization Authorization,
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
	public ICFBamDelTopDep moveBuffUp( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		ICFBamDelTopDep grandprev = null;
		ICFBamDelTopDep prev = null;
		ICFBamDelTopDep cur = null;
		ICFBamDelTopDep next = null;

		cur = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamDelTopDepByIdIdxKey",
				"CFBamDelTopDepByIdIdxKey",
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamBuffDelTopDep)cur );
		}

		prev = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamDelTopDepByIdIdxKey",
				"CFBamDelTopDepByIdIdxKey",
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamDelTopDepByIdIdxKey",
					"CFBamDelTopDepByIdIdxKey",
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamDelTopDepByIdIdxKey",
					"CFBamDelTopDepByIdIdxKey",
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		ICFBamDelTopDep newInstance;
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		ICFBamDelTopDep editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editCur = newInstance;
		editCur.set( cur );

		CFBamBuffDelTopDep editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamBuffDelTopDep editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
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
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffDelTopDep)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public ICFBamDelTopDep moveBuffDown( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamBuffDelTopDep prev = null;
		CFBamBuffDelTopDep cur = null;
		CFBamBuffDelTopDep next = null;
		CFBamBuffDelTopDep grandnext = null;

		cur = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamDelTopDepByIdIdxKey",
				"CFBamDelTopDepByIdIdxKey",
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamBuffDelTopDep)cur );
		}

		next = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"CFBamDelTopDepByIdIdxKey",
				"CFBamDelTopDepByIdIdxKey",
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamDelTopDepByIdIdxKey",
					"CFBamDelTopDepByIdIdxKey",
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableDelTopDep().readDerivedByIdIdx(Authorization, cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"CFBamDelTopDepByIdIdxKey",
					"CFBamDelTopDepByIdIdxKey",
					"Could not locate object.prev" );
			}
		}

		integer classCode = cur.getClassCode();
		CFBamBuffDelTopDep newInstance;
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		CFBamBuffDelTopDep editNext = newInstance;
		editNext.set( next );

		CFBamBuffDelTopDep editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamBuffDelTopDep editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				newInstance = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-instantiate-buff-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
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
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		classCode = editCur.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-cur-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		classCode = editNext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode == ICFBamDelTopDep.CLASS_CODE ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-update-grand-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		return( (CFBamBuffDelTopDep)editCur );
	}

	public ICFBamDelTopDep updateDelTopDep( ICFSecAuthorization Authorization,
		ICFBamDelTopDep iBuff )
	{
		CFBamBuffDelTopDep Buff = (CFBamBuffDelTopDep)schema.getTableDelDep().updateDelDep( Authorization,	Buff );
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffDelTopDep existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelTopDep",
				"Existing record not found",
				"Existing record not found",
				"DelTopDep",
				"DelTopDep",
				pkey );
		}
		CFBamBuffDelTopDepByDelTopDepTblIdxKey existingKeyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		existingKeyDelTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffDelTopDepByDelTopDepTblIdxKey newKeyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		newKeyDelTopDepTblIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamBuffDelTopDepByUNameIdxKey existingKeyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getFactoryDelTopDep().newByUNameIdxKey();
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelTopDepByUNameIdxKey newKeyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getFactoryDelTopDep().newByUNameIdxKey();
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamBuffDelTopDepByPrevIdxKey existingKeyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getFactoryDelTopDep().newByPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffDelTopDepByPrevIdxKey newKeyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getFactoryDelTopDep().newByPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamBuffDelTopDepByNextIdxKey existingKeyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getFactoryDelTopDep().newByNextIdxKey();
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamBuffDelTopDepByNextIdxKey newKeyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getFactoryDelTopDep().newByNextIdxKey();
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelTopDep",
					"DelTopDepUNameIdx",
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

		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdict;

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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
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
			subdict = new HashMap< CFLibDbKeyHash256, CFBamBuffDelTopDep >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		return(Buff);
	}

	public void deleteDelTopDep( ICFSecAuthorization Authorization,
		ICFBamDelTopDep iBuff )
	{
		final String S_ProcName = "CFBamRamDelTopDepTable.deleteDelTopDep() ";
		CFBamBuffDelTopDep Buff = ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffDelTopDep existing = dictByPKey.get( pkey );
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
		CFBamBuffTable container = (CFBamBuffTable)(schema.getTableTable().readDerivedByIdIdx( Authorization,
			varTableId ));
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		CFLibDbKeyHash256 prevId = existing.getOptionalPrevId();
		CFLibDbKeyHash256 nextId = existing.getOptionalNextId();

		CFBamBuffDelTopDep prev = null;
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
			CFBamBuffDelTopDep editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				editPrev = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editPrev.set( prev );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-prev-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		CFBamBuffDelTopDep next = null;
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
			CFBamBuffDelTopDep editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a81b" ) ) {
				editNext = schema.getFactoryDelTopDep().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-update-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
			editNext.set( next );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a81b" ) ) {
				schema.getTableDelTopDep().updateDelTopDep( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-delete-edit-next-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}

		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep1().readDerivedByDelTopDepIdx( Authorization,
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep1().deleteDelSubDep1ByDelTopDepIdx( Authorization,
						existing.getRequiredId() );
		}
		CFBamBuffDelTopDepByDelTopDepTblIdxKey keyDelTopDepTblIdx = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		keyDelTopDepTblIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamBuffDelTopDepByUNameIdxKey keyUNameIdx = (CFBamBuffDelTopDepByUNameIdxKey)schema.getFactoryDelTopDep().newByUNameIdxKey();
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamBuffDelTopDepByPrevIdxKey keyPrevIdx = (CFBamBuffDelTopDepByPrevIdxKey)schema.getFactoryDelTopDep().newByPrevIdxKey();
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamBuffDelTopDepByNextIdxKey keyNextIdx = (CFBamBuffDelTopDepByNextIdxKey)schema.getFactoryDelTopDep().newByNextIdxKey();
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffDelTopDep > subdict;

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
	public void deleteDelTopDepByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffDelTopDepByDelTopDepTblIdxKey key = (CFBamBuffDelTopDepByDelTopDepTblIdxKey)schema.getFactoryDelTopDep().newByDelTopDepTblIdxKey();
		key.setRequiredTableId( argTableId );
		deleteDelTopDepByDelTopDepTblIdx( Authorization, key );
	}

	public void deleteDelTopDepByDelTopDepTblIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByDelTopDepTblIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffDelTopDepByUNameIdxKey key = (CFBamBuffDelTopDepByUNameIdxKey)schema.getFactoryDelTopDep().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteDelTopDepByUNameIdx( Authorization, key );
	}

	public void deleteDelTopDepByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByUNameIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByPrevIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argPrevId )
	{
		CFBamBuffDelTopDepByPrevIdxKey key = (CFBamBuffDelTopDepByPrevIdxKey)schema.getFactoryDelTopDep().newByPrevIdxKey();
		key.setOptionalPrevId( argPrevId );
		deleteDelTopDepByPrevIdx( Authorization, key );
	}

	public void deleteDelTopDepByPrevIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByPrevIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByNextIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argNextId )
	{
		CFBamBuffDelTopDepByNextIdxKey key = (CFBamBuffDelTopDepByNextIdxKey)schema.getFactoryDelTopDep().newByNextIdxKey();
		key.setOptionalNextId( argNextId );
		deleteDelTopDepByNextIdx( Authorization, key );
	}

	public void deleteDelTopDepByNextIdx( ICFSecAuthorization Authorization,
		ICFBamDelTopDepByNextIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffDelDepByDefSchemaIdxKey key = (CFBamBuffDelDepByDefSchemaIdxKey)schema.getFactoryDelDep().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelTopDepByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelTopDepByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByDelDepIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argRelationId )
	{
		CFBamBuffDelDepByDelDepIdxKey key = (CFBamBuffDelDepByDelDepIdxKey)schema.getFactoryDelDep().newByDelDepIdxKey();
		key.setRequiredRelationId( argRelationId );
		deleteDelTopDepByDelDepIdx( Authorization, key );
	}

	public void deleteDelTopDepByDelDepIdx( ICFSecAuthorization Authorization,
		ICFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffDelTopDep cur;
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}

	public void deleteDelTopDepByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelTopDepByTenantIdx( Authorization, key );
	}

	public void deleteDelTopDepByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffDelTopDep cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffDelTopDep> matchSet = new LinkedList<CFBamBuffDelTopDep>();
		Iterator<CFBamBuffDelTopDep> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffDelTopDep> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffDelTopDep)(schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteDelTopDep( Authorization, cur );
		}
	}
}
