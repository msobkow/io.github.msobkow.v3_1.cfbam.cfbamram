
// Description: Java 25 in-memory RAM DbIO implementation for ServerProc.

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
 *	CFBamRamServerProcTable in-memory RAM DbIO implementation
 *	for ServerProc.
 */
public class CFBamRamServerProcTable
	implements ICFBamServerProcTable
{
	private ICFBamSchema schema;
	private Map< CFLibDbKeyHash256,
				CFBamBuffServerProc > dictByPKey
		= new HashMap< CFLibDbKeyHash256,
				CFBamBuffServerProc >();

	public CFBamRamServerProcTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public CFBamBuffScope ensureRec(ICFBamScope rec) {
		if (rec == null) {
			return( null );
		}
		else {
			return ((CFBamRamScopeTable)(schema.getTableScope())).ensureRec(rec);
		}
	}

	public ICFBamServerProc createServerProc( ICFSecAuthorization Authorization,
		ICFBamServerProc iBuff )
	{
		final String S_ProcName = "createServerProc";
		
		CFBamBuffServerProc Buff = (CFBamBuffServerProc)(schema.getTableServerMethod().createServerMethod( Authorization,
			iBuff ));
		CFLibDbKeyHash256 pkey;
		pkey = Buff.getRequiredId();
		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		if (Buff == null) {
			return( null );
		}
		else {
			int classCode = Buff.getClassCode();
			if (classCode == ICFBamServerProc.CLASS_CODE) {
				CFBamBuffServerProc retbuff = ((CFBamBuffServerProc)(schema.getFactoryServerProc().newRec()));
				retbuff.set(Buff);
				return( retbuff );
			}
			else {
				throw new CFLibUnsupportedClassException(getClass(), S_ProcName, "-create-buff-cloning-", (Integer)classCode, "Classcode not recognized: " + Integer.toString(classCode));
			}
		}
	}

	public ICFBamServerProc readDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readDerived";
		ICFBamServerProc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc lockDerived( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.lockDerived";
		ICFBamServerProc buff;
		if( dictByPKey.containsKey( PKey ) ) {
			buff = dictByPKey.get( PKey );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc[] readAllDerived( ICFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamServerProc.readAllDerived";
		ICFBamServerProc[] retList = new ICFBamServerProc[ dictByPKey.values().size() ];
		Iterator< CFBamBuffServerProc > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public ICFBamServerProc[] readDerivedByTenantIdx( ICFSecAuthorization Authorization,
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
			ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerProc ) ) {
					filteredList.add( (ICFBamServerProc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerProc[0] ) );
		}
	}

	public ICFBamServerProc readDerivedByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByUNameIdx";
		ICFBamServerMethod buff = schema.getTableServerMethod().readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( buff == null ) {
			return( null );
		}
		else if( buff instanceof ICFBamServerProc ) {
			return( (ICFBamServerProc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerProc[] readDerivedByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByMethTableIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByMethTableIdx( Authorization,
			TableId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerProc ) ) {
					filteredList.add( (ICFBamServerProc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerProc[0] ) );
		}
	}

	public ICFBamServerProc[] readDerivedByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readDerivedByDefSchemaIdx";
		ICFBamServerMethod buffList[] = schema.getTableServerMethod().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			ICFBamServerMethod buff;
			ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof ICFBamServerProc ) ) {
					filteredList.add( (ICFBamServerProc)buff );
				}
			}
			return( filteredList.toArray( new ICFBamServerProc[0] ) );
		}
	}

	public ICFBamServerProc readDerivedByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		ICFBamServerProc buff;
		if( dictByPKey.containsKey( Id ) ) {
			buff = dictByPKey.get( Id );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc readRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "CFBamRamServerProc.readRec";
		ICFBamServerProc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerProc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc lockRec( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 PKey )
	{
		final String S_ProcName = "lockRec";
		ICFBamServerProc buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( buff.getClassCode() != ICFBamServerProc.CLASS_CODE ) ) {
			buff = null;
		}
		return( buff );
	}

	public ICFBamServerProc[] readAllRec( ICFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamServerProc.readAllRec";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerProc.CLASS_CODE ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	public ICFBamServerProc readRecByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 Id )
	{
		final String S_ProcName = "CFBamRamScope.readRecByIdIdx() ";
		ICFBamServerProc buff = readDerivedByIdIdx( Authorization,
			Id );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
			return( (ICFBamServerProc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerProc[] readRecByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readRecByTenantIdx() ";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamScope.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerProc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	public ICFBamServerProc readRecByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByUNameIdx() ";
		ICFBamServerProc buff = readDerivedByUNameIdx( Authorization,
			TableId,
			Name );
		if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
			return( (ICFBamServerProc)buff );
		}
		else {
			return( null );
		}
	}

	public ICFBamServerProc[] readRecByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByMethTableIdx() ";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readDerivedByMethTableIdx( Authorization,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerProc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	public ICFBamServerProc[] readRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId )
	{
		final String S_ProcName = "CFBamRamServerMethod.readRecByDefSchemaIdx() ";
		ICFBamServerProc buff;
		ArrayList<ICFBamServerProc> filteredList = new ArrayList<ICFBamServerProc>();
		ICFBamServerProc[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && ( buff.getClassCode() == ICFBamServerMethod.CLASS_CODE ) ) {
				filteredList.add( (ICFBamServerProc)buff );
			}
		}
		return( filteredList.toArray( new ICFBamServerProc[0] ) );
	}

	/**
	 *	Read a page array of the specific ServerProc buffer instances identified by the duplicate key MethTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	TableId	The ServerProc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerProc[] pageRecByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 TableId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageRecByMethTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ServerProc buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	DefSchemaId	The ServerProc key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public ICFBamServerProc[] pageRecByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 DefSchemaId,
		CFLibDbKeyHash256 priorId )
	{
		final String S_ProcName = "pageRecByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public ICFBamServerProc updateServerProc( ICFSecAuthorization Authorization,
		ICFBamServerProc iBuff )
	{
		CFBamBuffServerProc Buff = (CFBamBuffServerProc)schema.getTableServerMethod().updateServerMethod( Authorization,	Buff );
		CFLibDbKeyHash256 pkey = Buff.getPKey();
		CFBamBuffServerProc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateServerProc",
				"Existing record not found",
				"Existing record not found",
				"ServerProc",
				"ServerProc",
				pkey );
		}
		// Check unique indexes

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateServerProc",
						"Superclass",
						"Superclass",
						"SuperClass",
						"SuperClass",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		// Update is valid

		Map< CFLibDbKeyHash256, CFBamBuffServerProc > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		return(Buff);
	}

	public void deleteServerProc( ICFSecAuthorization Authorization,
		ICFBamServerProc iBuff )
	{
		final String S_ProcName = "CFBamRamServerProcTable.deleteServerProc() ";
		CFBamBuffServerProc Buff = (CFBamBuffServerProc)ensureRec(iBuff);
		int classCode;
		CFLibDbKeyHash256 pkey = (CFLibDbKeyHash256)(Buff.getPKey());
		CFBamBuffServerProc existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteServerProc",
				pkey );
		}
					schema.getTableParam().deleteParamByServerMethodIdx( Authorization,
						existing.getRequiredId() );
		// Validate reverse foreign keys

		// Delete is valid
		Map< CFLibDbKeyHash256, CFBamBuffServerProc > subdict;

		dictByPKey.remove( pkey );

		schema.getTableServerMethod().deleteServerMethod( Authorization,
			Buff );
	}
	public void deleteServerProcByUNameIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId,
		String argName )
	{
		CFBamBuffServerMethodByUNameIdxKey key = (CFBamBuffServerMethodByUNameIdxKey)schema.getFactoryServerMethod().newByUNameIdxKey();
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteServerProcByUNameIdx( Authorization, key );
	}

	public void deleteServerProcByUNameIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByUNameIdxKey argKey )
	{
		CFBamBuffServerProc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerProc> matchSet = new LinkedList<CFBamBuffServerProc>();
		Iterator<CFBamBuffServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerProc)(schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByMethTableIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTableId )
	{
		CFBamBuffServerMethodByMethTableIdxKey key = (CFBamBuffServerMethodByMethTableIdxKey)schema.getFactoryServerMethod().newByMethTableIdxKey();
		key.setRequiredTableId( argTableId );
		deleteServerProcByMethTableIdx( Authorization, key );
	}

	public void deleteServerProcByMethTableIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByMethTableIdxKey argKey )
	{
		CFBamBuffServerProc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerProc> matchSet = new LinkedList<CFBamBuffServerProc>();
		Iterator<CFBamBuffServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerProc)(schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByDefSchemaIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argDefSchemaId )
	{
		CFBamBuffServerMethodByDefSchemaIdxKey key = (CFBamBuffServerMethodByDefSchemaIdxKey)schema.getFactoryServerMethod().newByDefSchemaIdxKey();
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteServerProcByDefSchemaIdx( Authorization, key );
	}

	public void deleteServerProcByDefSchemaIdx( ICFSecAuthorization Authorization,
		ICFBamServerMethodByDefSchemaIdxKey argKey )
	{
		CFBamBuffServerProc cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerProc> matchSet = new LinkedList<CFBamBuffServerProc>();
		Iterator<CFBamBuffServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerProc)(schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByIdIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamBuffServerProc cur;
		LinkedList<CFBamBuffServerProc> matchSet = new LinkedList<CFBamBuffServerProc>();
		Iterator<CFBamBuffServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerProc)(schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerProc( Authorization, cur );
		}
	}

	public void deleteServerProcByTenantIdx( ICFSecAuthorization Authorization,
		CFLibDbKeyHash256 argTenantId )
	{
		CFBamBuffScopeByTenantIdxKey key = (CFBamBuffScopeByTenantIdxKey)schema.getFactoryScope().newByTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteServerProcByTenantIdx( Authorization, key );
	}

	public void deleteServerProcByTenantIdx( ICFSecAuthorization Authorization,
		ICFBamScopeByTenantIdxKey argKey )
	{
		CFBamBuffServerProc cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamBuffServerProc> matchSet = new LinkedList<CFBamBuffServerProc>();
		Iterator<CFBamBuffServerProc> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamBuffServerProc> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = (CFBamBuffServerProc)(schema.getTableServerProc().readDerivedByIdIdx( Authorization,
				cur.getRequiredId() ));
			deleteServerProc( Authorization, cur );
		}
	}
}
