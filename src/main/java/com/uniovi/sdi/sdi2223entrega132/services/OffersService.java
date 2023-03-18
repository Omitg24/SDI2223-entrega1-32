package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.repositories.OffersRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OffersService {
    @Autowired
    private OffersRepository offersRepository;

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Método que devuelve las ofertas disponibles sin tener en cuentas las del usuario logeado.
     *
     * @param pageable pagina
     * @return lista con las ofertas disponibles
     */
    public Page<Offer> getAvailableOffers(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return offersRepository.findAllAvailableOffers(pageable, email);
    }

    /**
     * Método que devuelve las ofertas cuyo titulo contiene el texto pasado.
     *
     * @param pageable   pagina
     * @param searchText texto de busqueda para el titulo
     * @return lista de las ofertas cuyo titulo contiene el texto.
     */
    public Page<Offer> searchOffersByTitle(Pageable pageable, String searchText) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        searchText = "%" + searchText + "%";
        return offersRepository.searchByTitle(pageable, email, searchText);
    }

    /**
     * Método que añade al repositorio una oferta
     *
     * @param offer oferta
     */
    public void addOffer(Offer offer) {
        offersRepository.save(offer);
    }

    /**
     * Método que devuelve las ofertas correspondientes a un usuario
     *
     * @param pageable pagina
     * @param user     usuario
     * @return ofertas pertenecientes al usuario.
     */
    public Page<Offer> getOffersOfUser(Pageable pageable, User user) {
        return offersRepository.findAllByOwner(pageable, user);
    }

    /**
     * Método que devuelve todas las ofertas destacadas
     *
     * @return lista con las ofertas destacadas
     */
    public List<Offer> getOffersFeatured() {
        return offersRepository.findAllFeatured();
    }

    /**
     * Método que devuelve la oferta correspondiente al id proporcionado
     *
     * @param id id de la oferta
     * @return oferta correspondiente al id
     */
    public Offer getOfferById(Long id) {
        Optional<Offer> offer = offersRepository.findById(id);
        return offer.orElse(null);
    }

    /**
     * Método que elimina la oferta cuyo id es proporcionado
     *
     * @param id id de la oferta
     */
    public void deleteOffer(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (getOfferById(id).getOwner().getEmail().equals(email)) {
            offersRepository.deleteById(id);
        }
    }

    /**
     * Método que comprueba la validez de la compra de la oferta cuyo id es proporcionado
     *
     * @param id id de la oferta
     * @return verdadero si la compra es valida, falso en caso contrario
     */
    public boolean validatePurchase(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Offer> offer = offersRepository.findById(id);
        User user = usersRepository.findByEmail(email);
        return !(user.getAmount() >= (offer.map(Offer::getPrice).orElse(-1.0)));
    }

    /**
     * Método que comprueba la validez de desatacar una oferta
     *
     * @return verdadero si desatcar es valido, falso en caso contrario
     */
    public boolean validateFeature() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = usersRepository.findByEmail(email);
        return user.getAmount() < 20;
    }

    /**
     * Método que establece una oferta como comprada
     *
     * @param revised validez de la compra
     * @param id      id de la oferta
     */
    public void setOfferPurchase(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Offer offer = offersRepository.findById(id).get();
        User user = usersRepository.findByEmail(email);

        if (revised && !offer.isPurchase() && offer.getOwner().getEmail() != user.getEmail()) {
            if (user.getAmount() >= offer.getPrice()) {
                //Se actualiza el comprador de la oferta
                offer.setBuyer(user);
                offer.setPurchase(true);
                offersRepository.updateOffer(true, offer.getBuyer(), id);
                //Se le suma el dinero al vendedor
                usersRepository.updateAmount(offer.buyer.getAmount() + offer.getPrice(), offer.buyer.getId());
                //Se le resta el dinero al comprador
                usersRepository.updateAmount(user.getAmount() - offer.getPrice(), user.getId());
            }
        }
    }

    /**
     * Método que establece una oferta como destacada
     *
     * @param revised validez de destacar la oferta
     * @param id      id de la oferta
     */
    public void setOfferFeature(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Offer offer = offersRepository.findById(id).get();
        User user = usersRepository.findByEmail(email);

        if (revised && !offer.isFeatured() && offer.getOwner().getEmail() == user.getEmail()) {
            if (user.getAmount() >= 20.0) {
                offer.setFeatured(true);
                offersRepository.updateFeatured(true, id);
                //Se le resta el dinero al propietario
                usersRepository.updateAmount(user.getAmount() - 20.0, user.getId());
            }
        }
    }

    /**
     * Método que devuelve una lista con todas las ofertas compradas por un usuario
     *
     * @param user usuario correspondiente a las compras
     * @return lista con las ofertas compradas por el usuario
     */
    public List<Offer> getOffersOfBuyer(User user) {
        return offersRepository.findAllByBuyer(user);
    }

}
