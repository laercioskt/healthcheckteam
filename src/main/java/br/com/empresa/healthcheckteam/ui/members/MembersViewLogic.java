package br.com.empresa.healthcheckteam.ui.members;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.data2.Member;
import br.com.empresa.healthcheckteam.backend.repository.MemberRepository;
import com.vaadin.flow.component.UI;

import java.io.Serializable;
import java.util.Optional;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the member editor form and the data source, including
 * fetching and saving members.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class MembersViewLogic implements Serializable {

    private MemberRepository memberRepository;
    private final MembersView view;

    public MembersViewLogic(MemberRepository memberRepository, MembersView simpleCrudView) {
        this.memberRepository = memberRepository;
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewMemberEnabled(false);
        }
    }

    public void cancelMember() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the memberId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual member selections.
     */
    private void setFragmentParameter(String memberId) {
        String fragmentParameter;
        if (memberId == null || memberId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = memberId;
        }

        UI.getCurrent().navigate(MembersView.class, fragmentParameter);
    }

    /**
     * Opens the member form and clears its fields to make it ready for
     * entering a new member if memberId is null, otherwise loads the member
     * with the given memberId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param memberId
     */
    public void enter(String memberId) {
        if (memberId != null && !memberId.isEmpty()) {
            if (memberId.equals("new")) {
                newMember();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final Long pid = Long.parseLong(memberId);
                    findMember(pid).ifPresent(view::selectRow);
                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Optional<Member> findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public void saveMember(Member member) {
        final boolean newMember = member.isNew();
        view.clearSelection();
        view.updateMember(member);
        setFragmentParameter("");
        view.showNotification(member.getName() + (newMember ? " created" : " updated"));
    }

    public void deleteMember(Member member) {
        view.clearSelection();
        view.removeMember(member);
        setFragmentParameter("");
        view.showNotification(member.getName() + " removed");
    }

    public void editMember(Member member) {
        if (member == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(member.getId() + "");
        }
        view.editMember(member);
    }

    public void newMember() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editMember(new Member());
    }

    public void rowSelected(Member member) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editMember(member);
        }
    }
}
