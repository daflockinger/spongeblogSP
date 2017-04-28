package com.flockinger.spongeblogSP.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.history.Revision;
import org.springframework.stereotype.Component;

import com.flockinger.spongeblogSP.dao.VersionDAO;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.BaseModel;

@Component
public class VersioningService<M extends BaseModel, D extends VersionDAO<M>> {

	@Transactional
	public void rewind(Long id, D dao) throws NoVersionFoundException {
		Revision<Integer, M> latestRevision = null;
		
		if(dao.exists(id)){
			latestRevision = dao.findLastChangeRevision(id);
		}
	
		if (latestRevision == null) {
			throw new NoVersionFoundException("No version found for entity with id: " + id);
		}
		List<Revision<Integer, M>> revisions = dao.findRevisions(id).getContent();
		Optional<Revision<Integer, M>> previousRevision = fetchPreviousRevision(revisions,
				latestRevision.getRevisionNumber());

		if (previousRevision.isPresent()) {
			dao.save(previousRevision.get().getEntity());
		}
	}

	private Optional<Revision<Integer, M>> fetchPreviousRevision(List<Revision<Integer, M>> revisions,
			int latestRevNumber) {
		return revisions.stream().filter(revision -> revision.getRevisionNumber() == (latestRevNumber - 1)).findAny();
	}
}
