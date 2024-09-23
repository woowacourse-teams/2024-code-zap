from sqlalchemy import create_engine, Column, Integer, String, DateTime, Text, Boolean, ForeignKey
from sqlalchemy.orm import DeclarativeBase, sessionmaker
from faker import Faker
import random
from datetime import datetime
from typing import List, Optional

class Base(DeclarativeBase):
    pass

class Member(Base):
    __tablename__ = 'member'
    id: int = Column(Integer, primary_key=True)
    name: str = Column(String(255), unique=True, nullable=False)
    password: str = Column(String(255), nullable=False)
    salt: str = Column(String(255), nullable=False)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

class Category(Base):
    __tablename__ = 'category'
    id: int = Column(Integer, primary_key=True)
    name: str = Column(String(255), nullable=False)
    is_default: bool = Column(Boolean, nullable=False)
    member_id: int = Column(Integer, ForeignKey('member.id'), nullable=False)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

class Tag(Base):
    __tablename__ = 'tag'
    id: int = Column(Integer, primary_key=True)
    name: str = Column(String(255), nullable=False)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

class Template(Base):
    __tablename__ = 'template'
    id: int = Column(Integer, primary_key=True)
    title: str = Column(String(255), nullable=False)
    description: Optional[str] = Column(Text)
    category_id: int = Column(Integer, ForeignKey('category.id'), nullable=False)
    member_id: int = Column(Integer, ForeignKey('member.id'), nullable=False)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

class SourceCode(Base):
    __tablename__ = 'source_code'
    id: int = Column(Integer, primary_key=True)
    template_id: int = Column(Integer, ForeignKey('template.id'), nullable=False)
    ordinal: int = Column(Integer, nullable=False)
    content: str = Column(Text, nullable=False)
    filename: str = Column(String(255), nullable=False)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

class TemplateTag(Base):
    __tablename__ = 'template_tag'
    template_id: int = Column(Integer, ForeignKey('template.id'), primary_key=True)
    tag_id: int = Column(Integer, ForeignKey('tag.id'), primary_key=True)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

class Thumbnail(Base):
    __tablename__ = 'thumbnail'
    id: int = Column(Integer, primary_key=True)
    template_id: int = Column(Integer, ForeignKey('template.id'), unique=True, nullable=False)
    source_code_id: int = Column(Integer, ForeignKey('source_code.id'), unique=True, nullable=False)
    created_at: datetime = Column(DateTime, nullable=False)
    modified_at: datetime = Column(DateTime, nullable=False)

def create_members(session: sessionmaker, fake: Faker, count: int = 1) -> List[Member]:
    members = []
    used_names = set()
    while len(members) < count:
        name = fake.user_name()
        if name not in used_names:
            members.append(Member(
                name=name,
                password='ttAE37vCKnPOYer5RFJOKGdA8OkHqFSGpnUYxMNduHk=',
                salt='QQeYnrOdNc5SktHJfXWPFruGsiT+47WIykNQM2zwe9A=',
                created_at=fake.date_time_this_year(),
                modified_at=fake.date_time_this_month()
            ))
            used_names.add(name)
    session.add_all(members)
    session.commit()
    print(f"Created {count} members")
    return members

def create_categories(session: sessionmaker, fake: Faker, members: List[Member], categories_per_member: int = 10) -> List[Category]:
    categories = []
    used_names = set()
    for member in members:
        member_categories = 0
        while member_categories < categories_per_member:
            name = fake.word()
            if (member.id, name) not in used_names:
                categories.append(Category(
                    name=name,
                    is_default=(member_categories == 0),
                    member_id=member.id,
                    created_at=fake.date_time_this_year(),
                    modified_at=fake.date_time_this_month()
                ))
                used_names.add((member.id, name))
                member_categories += 1
    session.add_all(categories)
    session.commit()
    print(f"Created {len(categories)} categories")
    return categories

def create_tags(session: sessionmaker, fake: Faker, count: int = 200) -> List[Tag]:
    tags = [
        Tag(
            name=fake.word(),
            created_at=fake.date_time_this_year(),
            modified_at=fake.date_time_this_month()
        ) for _ in range(count)
    ]
    session.add_all(tags)
    session.commit()
    print(f"Created {count} tags")
    return tags

def create_template_batch(session: sessionmaker, fake: Faker, categories: List[Category], members: List[Member], tags: List[Tag], batch_size: int = 1000):
    templates = []
    source_codes = []
    template_tags = []
    thumbnails = []
    for _ in range(batch_size):
        template = Template(
            title=fake.sentence(),
            description=fake.text(),
            category_id=random.choice(categories).id,
            member_id=random.choice(members).id,
            created_at=fake.date_time_this_year(),
            modified_at=fake.date_time_this_month()
        )
        templates.append(template)
        session.add(template)
        session.flush()  # This will assign an id to the template

        # 항상 최소 1개의 소스 코드를 생성합니다
        batch_source_codes = [
            SourceCode(
                template_id=template.id,
                ordinal=j,
                content=fake.text(),
                filename=fake.file_name(),
                created_at=fake.date_time_this_year(),
                modified_at=fake.date_time_this_month()
            ) for j in range(max(1, random.randint(1, 5)))
        ]
        source_codes.extend(batch_source_codes)
        session.add_all(batch_source_codes)
        session.flush()  # 소스 코드에 id를 할당합니다

        # 수정된 부분: 중복 없는 태그 선택
        selected_tags = random.sample(tags, min(random.randint(1, 5), len(tags)))
        template_tags.extend([
            TemplateTag(
                template_id=template.id,
                tag_id=tag.id,
                created_at=fake.date_time_this_year(),
                modified_at=fake.date_time_this_month()
            ) for tag in selected_tags
        ])

        # 썸네일 생성 시 반드시 소스 코드가 존재함을 보장합니다
        thumbnails.append(Thumbnail(
            template_id=template.id,
            source_code_id=batch_source_codes[0].id,  # 첫 번째 소스 코드를 사용합니다
            created_at=fake.date_time_this_year(),
            modified_at=fake.date_time_this_month()
        ))

    session.add_all(template_tags)
    session.add_all(thumbnails)
    session.commit()
    print(f"Created {batch_size} templates with associated data")

def create_dummy_data(session: sessionmaker, fake: Faker):
    members = create_members(session, fake)
    categories = create_categories(session, fake, members)
    tags = create_tags(session, fake)
    total_templates = 10000
    batch_size = 1000
    for i in range(0, total_templates, batch_size):
        create_template_batch(session, fake, categories, members, tags, batch_size)

def main():
    engine = create_engine('mysql+pymysql://root:woowacourse@localhost:23306/code_zap', echo=False)
    Session = sessionmaker(bind=engine)
    session = Session()
    fake = Faker()
    for i in range(10):
      create_dummy_data(session, fake)
    session.close()

if __name__ == "__main__":
    main()
