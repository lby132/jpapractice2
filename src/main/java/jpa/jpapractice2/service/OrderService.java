package jpa.jpapractice2.service;

import jpa.jpapractice2.domain.OrderItem;
import jpa.jpapractice2.domain.OrderStatus;
import jpa.jpapractice2.domain.item.Delivery;
import jpa.jpapractice2.domain.item.Item;
import jpa.jpapractice2.domain.item.Member;
import jpa.jpapractice2.domain.item.Order;
import jpa.jpapractice2.repository.ItemRepository;
import jpa.jpapractice2.repository.MemberRepository;
import jpa.jpapractice2.repository.OrderRepository;
import jpa.jpapractice2.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final OrderRepository orderRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        final Member member = memberRepository.findOne(memberId);
        final Item item = itemRepository.findOne(itemId);

        final Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        final OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        final Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        final Order order = orderRepository.findOne(orderId);

        order.cancel();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
