package br.com.empresa.healthcheckteam.ui.members;

import br.com.empresa.healthcheckteam.backend.data2.Member;
import br.com.empresa.healthcheckteam.backend.repository.MemberRepository;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Member} entities.
 * <p>
 * Used to simplify the code in {@link MembersView} and
 * {@link MembersViewLogic}.
 */
public class MembersDataProvider extends ListDataProvider<Member> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";
    private final MemberRepository memberRepository;

    public MembersDataProvider(MemberRepository memberRepository) {
        super(memberRepository.findAll());
        this.memberRepository = memberRepository;
    }

    /**
     * Store given member to the backing data service.
     *
     * @param member the updated or new member
     */
    public void save(Member member) {
        final boolean newMember = member.isNew();

        memberRepository.save(member);
        if (newMember) {
            getItems().clear();
            getItems().addAll(memberRepository.findAll());
            refreshAll();
        } else {
            refreshItem(member);
        }
    }

    /**
     * Delete given member from the backing data service.
     *
     * @param member the member to be deleted
     */
    public void delete(Member member) {
        memberRepository.delete(member);
        getItems().clear();
        getItems().addAll(memberRepository.findAll());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for member name, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(member -> passesFilter(member.getName(), this.filterText));
    }

    @Override
    public Long getId(Member member) {
        Objects.requireNonNull(member, "Cannot provide an id for a null member.");

        return member.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
